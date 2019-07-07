package com.yoge.ad.service.search.mysql;

import com.yoge.ad.service.search.mysql.constant.OperateType;
import com.yoge.ad.service.search.mysql.dto.JsonTable;
import com.yoge.ad.service.search.mysql.dto.TableTemplate;
import com.yoge.ad.service.search.mysql.dto.Template;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/27
 */
@Data
public class ParseTemplate {

    private String databaseName;

    /**
     * key : tableName , value : TableTemplate
     */
    private Map<String, TableTemplate> tableTemplateMap = new HashMap<>();

    public static ParseTemplate parse(Template jsonTemplate) {
        ParseTemplate parseTemplate = new ParseTemplate();
        parseTemplate.setDatabaseName(jsonTemplate.getDatabase());
        for (JsonTable jsonTable : jsonTemplate.getTableList()) {
            String tableName = jsonTable.getTableName();
            Integer tableLevel = jsonTable.getLevel();
            TableTemplate tableTemplate = new TableTemplate();
            tableTemplate.setTableName(tableName);
            tableTemplate.setLevel(tableLevel);
            parseTemplate.getTableTemplateMap().put(tableName, tableTemplate);
            // 遍历操作类型对应的列
            Map<OperateType, List<String>> operateTypeFieldMap = tableTemplate.getOperateTypeFieldMap();
            jsonTable.getInsertList().forEach(column ->
                    getAndCreateIfNeed(
                            OperateType.ADD,
                            operateTypeFieldMap,
                            ArrayList::new
                            ).add(column.getColumnName()));
            jsonTable.getUpdateList().forEach(column ->
                    getAndCreateIfNeed(
                            OperateType.UPDATE,
                            operateTypeFieldMap,
                            ArrayList::new
                    ).add(column.getColumnName()));
            jsonTable.getDeleteList().forEach(column ->
                    getAndCreateIfNeed(
                            OperateType.DELETE,
                            operateTypeFieldMap,
                            ArrayList::new
                    ).add(column.getColumnName()));
        }
        return parseTemplate;
    }

    /**
     * 当map中不存在给定key时，则根据factory创建新的value且对应指定key，如存在当前key，则返回当前key对应的value
     * @return 当前key对应的value
     */
    private static <T, R> R getAndCreateIfNeed(T key, Map<T, R> map, Supplier<R> factory) {
        return map.computeIfAbsent(key, k -> factory.get());
    }

//    public static void main(String[] args) {
//        Map<OperateType, List<String>> operateTypeFieldMap = new HashMap<>();
//        operateTypeFieldMap.put(OperateType.ADD, new ArrayList<String>(Lists.newArrayList("aa")));
//        System.out.println(getAndCreateIfNeed(OperateType.ADD, operateTypeFieldMap, ArrayList::new).add("bb"));
//        System.out.println(getAndCreateIfNeed(OperateType.ADD, operateTypeFieldMap, ArrayList::new));
//    }
}
