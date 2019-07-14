package com.yoge.ad.service.search.mybatis;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yoge.ad.service.search.util.DateUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 使用mybatis时的通用SQL生成器
 *
 * @author geyoujia
 * @date 2019/07/12
 */
public class GeneralProvider {

    private static Map<Class<?>, EntityInfo> entityInfoMap = Maps.newConcurrentMap();

    public String genInsertSQL(Object object) {
        Preconditions.checkNotNull(object);
        EntityInfo entityInfo  =entityInfoMap.computeIfAbsent(object.getClass(), EntityInfo::new);
        SQL sql = new SQL();
        sql.INSERT_INTO(getTableName(entityInfo, object));
        for (FieldInfo fieldInfo : entityInfo.fieldInfos) {
            if (fieldInfo.isNotNull(object)) {
                sql.VALUES(fieldInfo.fieldName, String.format("#{%s}", fieldInfo.fieldName));
            }
        }
        return sql.toString();
    }

    public String genBatchInsertSQL(Object listObject, Object firstObject) {

        List objectList = JSON.parseArray(JSON.toJSONString(listObject), firstObject.getClass());

        Preconditions.checkNotNull(objectList);
        Preconditions.checkArgument(objectList.size() > 0);

        EntityInfo entityInfoEx = entityInfoMap.computeIfAbsent(firstObject.getClass(), EntityInfo::new);
        SQL sql = new SQL();
        sql.INSERT_INTO(getTableName(entityInfoEx, objectList.get(0)));

        String startSql = sql.toString();

        List<String> fieldNameList = Lists.newArrayList();

        for (FieldInfo fieldInfo : entityInfoEx.fieldInfos) {
            /** 主键不在列 **/
            if (fieldInfo.fieldName.equals(entityInfoEx.primaryKeyField.fieldName)) {
                continue;
            }

            if (fieldInfo.isNotNull(firstObject)) {
                fieldNameList.add(fieldInfo.fieldName);
            }
        }
        String fieldNameSql = String.format(" (%s) VALUES ", String.join(",", fieldNameList));
        List<String> valueSqlList = Lists.newArrayList();
        for (Object o : objectList) {
            EntityInfo entityInfo = entityInfoMap.computeIfAbsent(o.getClass(), EntityInfo::new);
            List<String> singValueSqlList = Lists.newArrayList();
            for (FieldInfo fieldInfo : entityInfo.fieldInfos) {
                /** 主键不在列 **/
                if (fieldInfo.fieldName.equals(entityInfoEx.primaryKeyField.fieldName)) {
                    continue;
                }
                /** 非空属性 **/
                if (fieldInfo.isNotNull(firstObject)) {

                    Field thsField = fieldInfo.field;
                    try {
                        /** 日期类型特殊处理 **/
                        if (thsField.getType() == Date.class) {
                            String datetime = DateUtil.format((Date) thsField.get(o));
                            singValueSqlList.add("'" + datetime + "'");
                        } else {
                            singValueSqlList.add("'" + String.valueOf(thsField.get(o) + "'"));
                        }
                    } catch (Exception e) {
                        singValueSqlList.add(null);
                    }
                }
            }
            String singValueSql = String.join(",", singValueSqlList);
            valueSqlList.add(String.format(" (%s) ", singValueSql));
        }
        String valueSql = String.join(",", valueSqlList);
        String finalSQL = startSql + fieldNameSql + valueSql;
        return finalSQL;
    }

    public String generateUpdateSQL(Object object) {
        Preconditions.checkNotNull(object);
        EntityInfo entityInfo = entityInfoMap.computeIfAbsent(object.getClass(), EntityInfo::new);
        if (!entityInfo.primaryKeyField.isNotNull(object)) {
            throw new RuntimeException("主键不能为空。Object:" + object);
        }
        SQL sql = new SQL();
        sql.UPDATE(getTableName(entityInfo, object));
        for (FieldInfo fieldInfo : entityInfo.fieldInfos) {
            if (fieldInfo.fieldName.equals(entityInfo.primaryKeyField.fieldName)) {
                continue;
            }
            if (fieldInfo.isNotNull(object)) {
                if (fieldInfo == entityInfo.versionField) {
                    sql.SET(String.format("%s = %s + 1", fieldInfo.fieldName, fieldInfo.fieldName));
                } else {
                    sql.SET(String.format("%s = #{%s}", fieldInfo.fieldName, fieldInfo.fieldName));
                }
            }
        }
        sql.WHERE(String.format("%s = #{%s}"), entityInfo.primaryKeyField.fieldName, entityInfo.primaryKeyField.fieldName);
        if (entityInfo.versionField != null && entityInfo.versionField.isNotNull(object)) {
            sql.WHERE(String.format("%s = #{%s}"), entityInfo.versionField.fieldName, entityInfo.versionField.fieldName);
        }
        return sql.toString();
    }


    private String getTableName(EntityInfo entityInfo, Object object) {
        return entityInfo.tableName;
    }

    private class EntityInfo {
        String tableName;
        FieldInfo primaryKeyField;
        FieldInfo versionField;

        List<FieldInfo> fieldInfos = Lists.newArrayList();

        /**
         * 如果使用mybatis的话实体类需要添加自定义的@Entity注解
         * @param clazz
         */
        @SneakyThrows
        EntityInfo(Class<?> clazz) {
            Entity entity = Preconditions.checkNotNull(clazz.getAnnotation(Entity.class), "未指定@Entity注解Class : " + clazz.getName());
            boolean tableExist = StringUtils.isNotEmpty(entity.table());
            Preconditions.checkArgument(tableExist, "未指定表名。Class : " + clazz.getName());
            if (tableExist) {
                tableName = entity.table();
            }
            while (!Object.class.equals(clazz)) {
                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isSynthetic() || field.getAnnotation(Transient.class) != null) {
                        continue;
                    }
                    FieldInfo fieldInfo = new FieldInfo(field);
                    fieldInfos.add(fieldInfo);
                    if (field.getName().equalsIgnoreCase(entity.primaryKey())) {
                        primaryKeyField = fieldInfo;
                    }
                    if (field.getName().equalsIgnoreCase(entity.versionControl())) {
                        versionField = fieldInfo;
                    }
                }
                clazz = clazz.getSuperclass();
            }
        }
    }

    private class FieldInfo {
        String fieldName;
        Field field;

        FieldInfo(Field field) {
            fieldName = field.getName();
            field.setAccessible(true);
            this.field = field;
        }

        boolean isNotNull(Object obj) {
            try {
                return field.get(obj) != null;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
