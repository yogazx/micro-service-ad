package com.yoge.ad.service.search.mysql.listener;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yoge.ad.service.search.mysql.TemplateHolder;
import com.yoge.ad.service.search.mysql.dto.BinlogRowData;
import com.yoge.ad.service.search.mysql.dto.TableTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/7/5
 */
@Slf4j
@Component
public class AggregationListener implements BinaryLogClient.EventListener {

    private String databaseName;
    private String tableName;

    private Map<String, IListener> listenerMap = Maps.newConcurrentMap();

    private final TemplateHolder templateHolder;

    @Autowired
    public AggregationListener(TemplateHolder templateHolder)  {
        this.templateHolder = templateHolder;
    }

    private String generateKey(String databaseName, String tableName) {
        return databaseName + ":" + tableName;
    }

    public void register(String databaseName, String tableName, IListener listener) {
        log.info("register : {}-{}", databaseName, tableName);
        listenerMap.put(generateKey(databaseName, tableName), listener);
    }

    /**
     * 实现 mysql-binlog-connector-java中提供的事件监听方法 对事件进行监听
     * 1. 对于mysql发生的任意事件, 记录下来事件对应的数据库和数据表
     * 2. 判断. 实际的业务逻辑是否需要关心该事件, 若关心则继续进行, 否则直接跳过并返回
     * 3. 拿到监听到的事件中的数据{@link EventData} 对其进行包装, 转化为业务逻辑自定义实体 {@link BinlogRowData}
     * 4. 对于包装后的 {@link BinlogRowData} 使用自定义的 业务事件监听器{@link IListener} 进行处理, 对于检索服务来说
     * 对{@link BinlogRowData}的处理方式就是实现对增量数据的一次更新
     *
     * @param event
     */
    @Override
    public void onEvent(Event event) {
        EventType eventType = event.getHeader().getEventType();
        log.debug("event type : {}", eventType);
        // 如果是TABLE_MAP_EVENT，则不做处理
        if (eventType == EventType.TABLE_MAP) {
            TableMapEventData eventData = event.getData();
            this.tableName = eventData.getTable();
            this.databaseName = eventData.getDatabase();
            return;
        }
        // 如果不是write,update,delete中的任何一种操作，则不做处理
        if (!EventType.isRowMutation(eventType)) {
            return;
        }
        if (StringUtils.isBlank(databaseName) || StringUtils.isBlank(tableName)) {
            log.error("no meta data event");
        }
        String key = generateKey(databaseName, tableName);
        IListener listener = this.listenerMap.get(key);
        // 没有关心该表的监听器
        if (listener == null) {
            log.debug("no listener, skip {}", key);
            return;
        }
        log.info("trigger event: {}", eventType.name());
        try {
            // 将eventData中的属性转化为自定义的BinlogRowData
            BinlogRowData binlogRowData = buildRowData(event.getData());
            if (binlogRowData == null)
                return;
            binlogRowData.setEventType(eventType);
            listener.onEvent(binlogRowData);
        } catch (Exception e) {
            log.error("binlogRowData 构造失败");
        } finally {
            this.databaseName = StringUtils.EMPTY;
            this.tableName = StringUtils.EMPTY;
        }

    }

    /**
     * 一次单行insert事件后, 产生的eventData如下
     * WriteRowsEventData{tableId=122, includedColumns={0, 1, 2}, rows=[[13, 10, 标志]]},
     *
     * 一次单行update事件后，产生的eventData如下
     *UpdateRowsEventData{tableId=108, includedColumnsBeforeUpdate={0, 1, 2, 3, 4, 5}, includedColumns={0, 1, 2, 3, 4, 5}, rows=[
     *     {before=[2, Sat Jul 06 13:59:14 CST 2019, 15, Sat Jul 06 13:59:22 CST 2019, 1, info], after=[2, Sat Jul 06 13:59:14 CST 2019, 15, Sat Jul 06 13:59:22 CST 2019, 2, cc]}
     * ]}
     *
     * @param eventData
     * @return
     */
    private BinlogRowData buildRowData(EventData eventData) {
        // 得到当前表对应的tableTemplate
        TableTemplate tableTemplate = templateHolder.getTableTemplate(this.tableName);
        if (tableTemplate == null) {
            log.warn("未找到当前表");
            return null;
        }
        BinlogRowData rowData = new BinlogRowData();
        if (eventData instanceof UpdateRowsEventData) {
            // 得到update前的数据
            List<Map<String, String>> beforeMapList = Lists.newArrayList();
            List<Serializable[]> beforeValueList = ((UpdateRowsEventData) eventData).getRows().stream().map(Map.Entry::getKey).collect(Collectors.toList());
            for (Serializable[] beforeValues : beforeValueList) {
                Map<String, String> beforeMap = Maps.newHashMap();
                int length = beforeValues.length;
                for (int i = 0; i < length; i++) {
                    // 字段索引映射到字段名
//                    String columnName = tableTemplate.getPositionMap().get(i);
//                    if (StringUtils.isBlank(columnName)) {
//                        log.debug("当前列未找到,忽略该position:{}", i);
//                        continue;
//                    }
//                    String columnValue = beforeValues[i].toString();
//                    beforeMap.put(columnName, columnValue);
                    beforeMap = generateColumnName2ColumnValue(beforeMap, tableTemplate, i, beforeValues);
                    log.debug("before update : {}", beforeMap);
                }
                beforeMapList.add(beforeMap);
            }
            rowData.setBefore(beforeMapList);
        }
        List<Map<String, String>> afterMapList = Lists.newArrayList();
        List<Serializable[]> afterValueList = getAfterValues(eventData);
        for (Serializable[] afterValues : afterValueList) {
            Map<String, String> afterMap = Maps.newHashMap();
            int length = afterValues.length;
            for (int i = 0; i < length; i++) {
//                String columnName = tableTemplate.getPositionMap().get(i);
//                if (StringUtils.isBlank(columnName)) {
//                    log.debug("当前列未找到,忽略该position:{}", i);
//                    continue;
//                }
//                String columnValue = afterValues[i].toString();
//                afterMap.put(columnName, columnValue);
                afterMap = generateColumnName2ColumnValue(afterMap, tableTemplate, i, afterValues);
                log.debug("after update : {}", afterMap);
            }
            afterMapList.add(afterMap);
        }
        rowData.setAfter(afterMapList);
        rowData.setTableTemplate(tableTemplate);
        return rowData;
    }

    private Map<String, String> generateColumnName2ColumnValue(Map<String, String> map, TableTemplate tableTemplate, int index, Serializable[] values) {
        String columnName = tableTemplate.getPositionMap().get(index);
        if (StringUtils.isBlank(columnName)) {
            log.debug("当前列未找到,忽略该position:{}", index);
        }
        String columnValue = values[index].toString();
        map.put(columnName, columnValue);
        return map;
    }

    private List<Serializable[]> getAfterValues(EventData eventData) {
        // insert event
        if (eventData instanceof WriteRowsEventData) {
            return ((WriteRowsEventData) eventData).getRows();
        }
        // update event
        if (eventData instanceof UpdateRowsEventData) {
            return ((UpdateRowsEventData) eventData).getRows().stream().map(Map.Entry::getValue).collect(Collectors.toList());
        }
        // delete event
        if (eventData instanceof DeleteRowsEventData) {
            return ((DeleteRowsEventData) eventData).getRows();
        }
        return Collections.emptyList();
    }
}
