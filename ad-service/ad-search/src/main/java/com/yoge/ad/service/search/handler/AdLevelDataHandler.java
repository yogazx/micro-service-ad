package com.yoge.ad.service.search.handler;

import com.alibaba.fastjson.JSON;
import com.yoge.ad.service.common.dump.table.*;
import com.yoge.ad.service.search.index.DataTable;
import com.yoge.ad.service.search.index.District.UnitDistrictIndex;
import com.yoge.ad.service.search.index.IndexAware;
import com.yoge.ad.service.search.index.adPlan.AdPlanIndex;
import com.yoge.ad.service.search.index.adPlan.AdPlanObject;
import com.yoge.ad.service.search.index.adUnit.AdUnitIndex;
import com.yoge.ad.service.search.index.adUnit.AdUnitObject;
import com.yoge.ad.service.search.index.creative.CreativeIndex;
import com.yoge.ad.service.search.index.creative.CreativeObject;
import com.yoge.ad.service.search.index.creativeUnit.CreativeUnitIndex;
import com.yoge.ad.service.search.index.creativeUnit.CreativeUnitObject;
import com.yoge.ad.service.search.index.interest.UnitItIndex;
import com.yoge.ad.service.search.index.unitKeyword.UnitKeywordIndex;
import com.yoge.ad.service.search.mysql.constant.OperateType;
import com.yoge.ad.service.search.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * DESC 1.因为索引之间存在依赖关系，比如推广单元需要有推广计划的存在。 即索引之间存在着层级的划分 ： 用户层级 ---> 推广计划 --->推广单元 ---->推广单元限制...
 * 2. 全量索引其实是 增量索引 "添加" 的一种特殊实现
 *
 * @author You Jia Ge
 * Created Time 2019/5/23
 */
@Slf4j
public class AdLevelDataHandler {

    public static void handleLevel2WithPlan(AdPlanTable adPlanTable, OperateType type) {
        AdPlanObject planObject = new AdPlanObject(adPlanTable.getId(), adPlanTable.getUserId(), adPlanTable.getPlanStatus(), adPlanTable.getStartDate(), adPlanTable.getEndDate());
        handleBinlogEvent(
                DataTable.of(AdPlanIndex.class),
                planObject.getPlanId(),
                planObject,
                type
        );
    }

    public static void handleLevel2WithCreative(AdCreativeTable adCreativeTable, OperateType type) {
        CreativeObject creativeObject = new CreativeObject(adCreativeTable.getId(), adCreativeTable.getName(), adCreativeTable.getType(), adCreativeTable.getMaterialType(), adCreativeTable.getHeight(), adCreativeTable.getWidth(), adCreativeTable.getAuditStatus(), adCreativeTable.getAdUrl());
        handleBinlogEvent(
                DataTable.of(CreativeIndex.class),
                creativeObject.getAdId(),
                creativeObject,
                type
        );
    }

    public static void handleLevel3WithUnit(AdUnitTable adUnitTable, OperateType type) {
        // 从redis中获取adPlan
        AdPlanObject planObject = DataTable.of(AdPlanIndex.class).get(adUnitTable.getPlanId());
        if (planObject == null) {
            log.error("error! handleLevel3 found adPlanObject is not exist! {}", adUnitTable.getPlanId());
            return;
        }
        AdUnitObject unitObject = new AdUnitObject(adUnitTable.getId(), adUnitTable.getUnitStatus(), adUnitTable.getPositionType(), adUnitTable.getPlanId(), planObject);
        handleBinlogEvent(
                DataTable.of(AdUnitIndex.class),
                unitObject.getUnitId(),
                unitObject,
                type
        );
    }

    public static void handleLevel3WithCreativeUnit(AdCreativeUnitTable creativeUnitTable, OperateType type) {
        // todo 这里因为目前的creativeUnit索引中不支持update,待完成
        if (type == OperateType.UPDATE) {
            log.error("CreativeUnitIndex not support update");
            return;
        }
        // 获取推广单元对象和创意对象
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(creativeUnitTable.getUnitId());
        CreativeObject creativeObject = DataTable.of(CreativeIndex.class).get(creativeUnitTable.getId());
        if (unitObject == null || creativeObject == null) {
            log.error("AdCreativeUnitTable index error, unitObject or creativeObject is null, {}", JSON.toJSONString(creativeUnitTable));
            return;
        }
        CreativeUnitObject creativeUnitObject = new CreativeUnitObject(creativeUnitTable.getId(), creativeUnitTable.getUnitId());
        handleBinlogEvent(
                DataTable.of(CreativeUnitIndex.class),
                CommonUtils.stringContact(creativeUnitObject.getAdId().toString(), creativeUnitObject.getUnitId().toString()),
                creativeUnitObject,
                type
        );
    }

    public static void handleLevel4WithUnitDistrict(AdUnitDistrictTable unitDistrictTable, OperateType type) {
        // todo 这里因为目前的unitDistrict索引中不支持update,待完成
        if (type == OperateType.UPDATE) {
            log.error("unitDistrictIndex not support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitDistrictTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitDistrictTable index error! unitObject is null: {}", JSON.toJSONString(unitDistrictTable));
            return;
        }
        String redisKey = CommonUtils.stringContact(unitDistrictTable.getProvince(), unitDistrictTable.getCity());
        Set<Long> redisValue = new HashSet<>(Collections.singleton(unitDistrictTable.getUnitId()));
        handleBinlogEvent(
                DataTable.of(UnitDistrictIndex.class),
                redisKey,
                redisValue,
                type
        );
    }

    public static void handleLevel4WithUnitIt(AdUnitItTable unitItTable, OperateType type) {
        // todo 待完成
        if (type == OperateType.UPDATE) {
            log.error("it index not support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitItTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitItTable index error unitObject is null, {}", unitItTable.getUnitId());
            return;
        }
        Set<Long> redisValue = new HashSet<>(Collections.singleton(unitItTable.getUnitId()));
        handleBinlogEvent(
                DataTable.of(UnitItIndex.class),
                unitItTable.getItTag(),
                redisValue,
                type
        );
    }

    public static void handleLevel4WithUnitKeyword(AdUnitKeywordTable unitKeywordTable, OperateType type) {
        if (type == OperateType.UPDATE) {
            log.error("keyword index update is not supported");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitKeywordTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitKeywordTable index error: unitObject is not exist, {}", unitKeywordTable.getUnitId());
            return;
        }
        Set<Long> redisValue = new HashSet<>(Collections.singleton(unitKeywordTable.getUnitId()));
        handleBinlogEvent(
                DataTable.of(UnitKeywordIndex.class),
                unitKeywordTable.getKeyword(),
                redisValue,
                type
        );
    }

    /**
     * 构造增量索引或加载全量索引
     *
     * @param indexAware 索引操作接口的实现类
     * @param key        索引key
     * @param value      索引value
     * @param type       对于索引的操作：增删改
     * @param <K>
     * @param <V>
     */
    private static <K, V> void handleBinlogEvent(IndexAware<K, V> indexAware, K key, V value, OperateType type) {
        switch (type) {
            case ADD:
                indexAware.add(key, value);
                break;
            case UPDATE:
                indexAware.update(key, value);
                break;
            case DELETE:
                indexAware.delete(key, value);
                break;
            default:
                break;
        }
    }
}
