package com.yoge.ad.service.search.sender.redis;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.yoge.ad.service.common.dump.table.*;
import com.yoge.ad.service.search.handler.AdLevelDataHandler;
import com.yoge.ad.service.search.index.DataLevel;
import com.yoge.ad.service.search.mysql.constant.DBConstant;
import com.yoge.ad.service.search.mysql.dto.MysqlRowData;
import com.yoge.ad.service.search.sender.ISender;
import com.yoge.ad.service.search.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 广告增量数据的投放数据(通过调用{@link com.yoge.ad.service.search.handler.AdLevelDataHandler}将索引数据保存在redis中)
 *
 * @author geyoujia
 * @date 2019/07/13
 */
@Slf4j
@Component
public class RedisIndexSender implements ISender {

    @Override
    public void send(MysqlRowData mysqlRowData) {
        Integer level = mysqlRowData.getLevel();
        if (level != null && level > 1) {
            if (level.equals(DataLevel.LEVEL_TWO.getLevel())) {
                level2RowData(mysqlRowData);
            } else if (level.equals(DataLevel.LEVEL_THREE.getLevel())) {
                level3RowData(mysqlRowData);
            } else if (level.equals(DataLevel.LEVEL_FOUR.getLevel())) {
                level4RowData(mysqlRowData);
            } else {
                log.error("增量数据level不存在，{}", JSON.toJSONString(mysqlRowData));
            }
        }
    }

    /**
     * 针对第二层级增量数据投递
     * ad_plan 推广计划
     * ad_creative 创意
     *
     * @param mysqlRowData
     */
    private void level2RowData(MysqlRowData mysqlRowData) {
        if (mysqlRowData.getTableName().equals(DBConstant.AD_PLAN_TABLE_INFO.TABLE_NAME)) {
            List<AdPlanTable> adPlanTableList = Lists.newArrayList();
            mysqlRowData.getFieldValueMapList().forEach(fieldValueMap -> {
                AdPlanTable adPlanTable = new AdPlanTable();
                fieldValueMap.forEach((columnName, columnValue) -> {
                    switch (columnName) {
                        case DBConstant.AD_PLAN_TABLE_INFO.COLUMN_ID:
                            adPlanTable.setId(Long.valueOf(columnValue));
                            break;
                        case DBConstant.AD_PLAN_TABLE_INFO.COLUMN_USER_ID:
                            adPlanTable.setUserId(Long.valueOf(columnValue));
                            break;
                        case DBConstant.AD_PLAN_TABLE_INFO.COLUMN_PLAN_STATUS:
                            adPlanTable.setPlanStatus(Integer.valueOf(columnValue));
                            break;
                        case DBConstant.AD_PLAN_TABLE_INFO.COLUMN_START_DATE:
                            adPlanTable.setStartDate(CommonUtils.stringToLocalDateTime(columnValue));
                            break;
                        case DBConstant.AD_PLAN_TABLE_INFO.COLUMN_END_DATE:
                            adPlanTable.setEndDate(CommonUtils.stringToLocalDateTime(columnValue));
                            break;
                        default:
                            log.warn("not match a valid column, ignored");
                            break;
                    }
                });
                adPlanTableList.add(adPlanTable);
            });
            adPlanTableList.forEach(adPlanTable -> AdLevelDataHandler.handleLevel2WithPlan(adPlanTable, mysqlRowData.getOperateType()));
        } else if (mysqlRowData.getTableName().equals(DBConstant.AD_CREATIVE_TABLE_INFO.TABLE_NAME)) {
            List<AdCreativeTable> creativeTableList = Lists.newArrayList();
            mysqlRowData.getFieldValueMapList().forEach(fieldValueMap -> {
                AdCreativeTable adCreativeTable = new AdCreativeTable();
                fieldValueMap.forEach((columnName, columnValue) -> {
                    switch (columnName) {
                        case DBConstant.AD_CREATIVE_TABLE_INFO.COLUMN_ID:
                            adCreativeTable.setId(Long.valueOf(columnValue));
                            break;
                        case DBConstant.AD_CREATIVE_TABLE_INFO.COLUMN_NAME:
                            adCreativeTable.setName(columnValue);
                            break;
                        case DBConstant.AD_CREATIVE_TABLE_INFO.COLUMN_AUDIT_STATUS:
                            adCreativeTable.setAuditStatus(Integer.valueOf(columnValue));
                            break;
                        case DBConstant.AD_CREATIVE_TABLE_INFO.COLUMN_height:
                            adCreativeTable.setHeight(Integer.valueOf(columnValue));
                            break;
                        case DBConstant.AD_CREATIVE_TABLE_INFO.COLUMN_MATERIAL_TYPE:
                            adCreativeTable.setMaterialType(Integer.valueOf(columnValue));
                            break;
                        case DBConstant.AD_CREATIVE_TABLE_INFO.COLUMN_TYPE:
                            adCreativeTable.setType(Integer.valueOf(columnValue));
                            break;
                        case DBConstant.AD_CREATIVE_TABLE_INFO.COLUMN_URL:
                            adCreativeTable.setAdUrl(columnValue);
                            break;
                        case DBConstant.AD_CREATIVE_TABLE_INFO.COLUMN_width:
                            adCreativeTable.setWidth(Integer.valueOf(columnValue));
                            break;
                        default:
                            log.warn("not match a valid column, ignored");
                            break;
                    }
                });
                creativeTableList.add(adCreativeTable);
            });
            creativeTableList.forEach(adCreativeTable -> AdLevelDataHandler.handleLevel2WithCreative(adCreativeTable, mysqlRowData.getOperateType()));
        }
    }

    /**
     * 针对第三层级增量数据投递
     * ad_unit          推广单元
     * creative_unit    退广单元-创意关联
     *
     * @param mysqlRowData
     */
    public void level3RowData(MysqlRowData mysqlRowData) {
        if (mysqlRowData.getTableName().equals(DBConstant.AD_UNIT_TABLE_INFO.TABLE_NAME)) {
            List<AdUnitTable> unitTableList = Lists.newArrayList();
            mysqlRowData.getFieldValueMapList().forEach(fieldValueMap -> {
                AdUnitTable adUnitTable = new AdUnitTable();
                fieldValueMap.forEach((columnName, columnValue) -> {
                    switch (columnName) {
                        case DBConstant.AD_UNIT_TABLE_INFO.COLUMN_ID:
                            adUnitTable.setId(Long.valueOf(columnValue));
                            break;
                        case DBConstant.AD_UNIT_TABLE_INFO.COLUMN_PLAN_ID:
                            adUnitTable.setPlanId(Long.valueOf(columnValue));
                            break;
                        case DBConstant.AD_UNIT_TABLE_INFO.COLUMN_UNIT_STATUS:
                            adUnitTable.setUnitStatus(Integer.valueOf(columnValue));
                            break;
                        case DBConstant.AD_UNIT_TABLE_INFO.COLUMN_POSITION_TYPE:
                            adUnitTable.setPositionType(Integer.valueOf(columnValue));
                            break;
                        default:
                            log.warn("not matched column, ignored");
                            break;
                    }
                });
                unitTableList.add(adUnitTable);
            });
            unitTableList.forEach(adUnitTable -> AdLevelDataHandler.handleLevel3WithUnit(adUnitTable, mysqlRowData.getOperateType()));
        } else if (mysqlRowData.getTableName().equals(DBConstant.AD_CREATIVE_UNIT_TABLE_INFO.TABLE_NAME)) {
            List<AdCreativeUnitTable> creativeUnitTables = Lists.newArrayList();
            mysqlRowData.getFieldValueMapList().forEach(fieldValueMap -> {
                AdCreativeUnitTable creativeUnitTable = new AdCreativeUnitTable();
                fieldValueMap.forEach((columnName, columnValue) -> {
                    switch (columnName) {
                        case DBConstant.AD_CREATIVE_UNIT_TABLE_INFO.COLUMN_CREATIVE_ID:
                            creativeUnitTable.setId(Long.valueOf(columnValue));
                            break;
                        case DBConstant.AD_CREATIVE_UNIT_TABLE_INFO.COLUMN_UNIT_ID:
                            creativeUnitTable.setUnitId(Long.valueOf(columnValue));
                            break;
                        default:
                            log.warn("not match a valid column, ignored");
                            break;
                    }
                });
                creativeUnitTables.add(creativeUnitTable);
            });
            creativeUnitTables.forEach(creativeUnitTable -> {
                AdLevelDataHandler.handleLevel3WithCreativeUnit(creativeUnitTable, mysqlRowData.getOperateType());
            });
        }
    }

    /**
     * 针对第四层级增量数据投递
     * ad_unit_it           推广单元 - 兴趣关联
     * ad_unit_district     推广单元 - 地域关联
     * ad_unit_keyword      推广单元 - 关键词关联
     *
     * @param mysqlRowData
     */
    public void level4RowData(MysqlRowData mysqlRowData) {
        if (mysqlRowData.getTableName().equals(DBConstant.AD_UNIT_IT_TABLE_INFO.TABLE_NAME)) {
            List<AdUnitItTable> unitItTableList = Lists.newArrayList();
            mysqlRowData.getFieldValueMapList().forEach(fieldValueMap -> {
                AdUnitItTable adUnitItTable = new AdUnitItTable();
                fieldValueMap.forEach((columnName, columnValue) -> {
                    switch (columnName) {
                        case DBConstant.AD_UNIT_IT_TABLE_INFO.COLUMN_UNIT_ID:
                            adUnitItTable.setUnitId(Long.valueOf(columnValue));
                            break;
                        case DBConstant.AD_UNIT_IT_TABLE_INFO.COLUMN_IT_TAG:
                            adUnitItTable.setItTag(columnValue);
                            break;
                        default:
                            log.warn("not match a valid column, ignored");
                            break;
                    }
                });
                unitItTableList.add(adUnitItTable);
            });
            unitItTableList.forEach(adUnitItTable -> AdLevelDataHandler.handleLevel4WithUnitIt(adUnitItTable, mysqlRowData.getOperateType()));
        } else if (mysqlRowData.getTableName().equals(DBConstant.AD_UNIT_DISTRICT_TABLE_INFO.TABLE_NAME)) {
            List<AdUnitDistrictTable> unitDistrictTableList = Lists.newArrayList();
            mysqlRowData.getFieldValueMapList().forEach(fieldValueMap -> {
                AdUnitDistrictTable unitDistrictTable = new AdUnitDistrictTable();
                fieldValueMap.forEach((columnName, columnValue) -> {
                    switch (columnName) {
                        case DBConstant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_UNIT_ID:
                            unitDistrictTable.setUnitId(Long.valueOf(columnValue));
                            break;
                        case DBConstant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_PROVINCE:
                            unitDistrictTable.setProvince(columnValue);
                            break;
                        case DBConstant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_CITY:
                            unitDistrictTable.setCity(columnValue);
                            break;
                        default:
                            log.warn("not match a valid column, ignored");
                            break;
                    }
                });
                unitDistrictTableList.add(unitDistrictTable);
            });
            unitDistrictTableList.forEach(adUnitDistrictTable -> AdLevelDataHandler.handleLevel4WithUnitDistrict(adUnitDistrictTable, mysqlRowData.getOperateType()));
        } else if (mysqlRowData.getTableName().equals(DBConstant.AD_UNIT_KEYWORD_TABLE_INFO.TABLE_NAME)) {
            List<AdUnitKeywordTable> unitKeywordTableList = Lists.newArrayList();
            mysqlRowData.getFieldValueMapList().forEach(fieldValueMap -> {
                AdUnitKeywordTable unitKeywordTable = new AdUnitKeywordTable();
                fieldValueMap.forEach((columnName, columnValue) -> {
                    switch (columnName) {
                        case DBConstant.AD_UNIT_KEYWORD_TABLE_INFO.COLUMN_UNIT_ID:
                            unitKeywordTable.setUnitId(Long.valueOf(columnValue));
                            break;
                        case DBConstant.AD_UNIT_KEYWORD_TABLE_INFO.COLUMN_KEYWORD:
                            unitKeywordTable.setKeyword(columnValue);
                            break;
                        default:
                            log.warn("not match a valid column, ignored");
                            break;
                    }
                });
                unitKeywordTableList.add(unitKeywordTable);
            });
            unitKeywordTableList.forEach(adUnitKeywordTable -> AdLevelDataHandler.handleLevel4WithUnitKeyword(adUnitKeywordTable, mysqlRowData.getOperateType()));
        }
    }
}
