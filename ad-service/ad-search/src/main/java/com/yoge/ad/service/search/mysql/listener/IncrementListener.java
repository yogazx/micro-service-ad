package com.yoge.ad.service.search.mysql.listener;

import com.github.shyiko.mysql.binlog.event.EventType;
import com.yoge.ad.service.search.mysql.constant.DBConstant;
import com.yoge.ad.service.search.mysql.constant.OperateType;
import com.yoge.ad.service.search.mysql.dto.BinlogRowData;
import com.yoge.ad.service.search.mysql.dto.MysqlRowData;
import com.yoge.ad.service.search.mysql.dto.TableTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 注册需要监听的表，同时将增量数据投送到Kafka
 * @author geyoujia
 * @date 2019/07/06
 */
@Component
public class IncrementListener implements IListener {

    public static final Logger LOGGER = LoggerFactory.getLogger(IncrementListener.class);

    @Autowired
    private AggregationListener aggregationListener;

    @PostConstruct
    @Override
    public void register() {
        LOGGER.info("IncrementListener register table info");
        DBConstant.TABLE_2_DB.forEach((key, value) -> aggregationListener.register(value, key, this));
    }

    @Override
    public void onEvent(BinlogRowData binlogRowData) {
        TableTemplate tableTemplate = binlogRowData.getTableTemplate();
        EventType eventType = binlogRowData.getEventType();
        MysqlRowData mysqlRowData = new MysqlRowData();
        mysqlRowData.setTableName(tableTemplate.getTableName());
        mysqlRowData.setLevel(tableTemplate.getLevel());
        mysqlRowData.setOperateType(OperateType.of(eventType));
        // todo
    }
}
