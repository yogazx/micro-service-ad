package com.yoge.ad.service.search.index.adUnit;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.yoge.ad.service.search.index.IndexAware;
import com.yoge.ad.service.search.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * DESC 推广单元索引实现
 *
 * @author You Jia Ge
 * Created Time 2019/5/16
 */
@Slf4j
@Component
public class AdUnitIndex implements IndexAware<Long, AdUnitObject> {

    /**
     * 推广单元索引 redis_key 前缀
     */
    private static final String AD_UNIT_INDEX_PREFIX = "ad_unit_index_prefix_";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public AdUnitObject get(Long key) {
        String redisKey = AD_UNIT_INDEX_PREFIX + key;
        return (AdUnitObject) redisTemplate.opsForValue().get(redisKey);
    }

    @Override
    public void add(Long key, AdUnitObject value) {
        String redisKey = AD_UNIT_INDEX_PREFIX + key;
        log.info("AdUnitIndex, before add the key set is {}", redisTemplate.keys(AD_UNIT_INDEX_PREFIX + "*"));
        redisTemplate.opsForValue().set(redisKey, value);
        log.info("AdUnitIndex, after add the key set is {}", redisTemplate.keys(AD_UNIT_INDEX_PREFIX + "*"));
    }

    @Override
    public void update(Long key, AdUnitObject value) {
        String redisKey = AD_UNIT_INDEX_PREFIX + key;
        log.info("AdUnitIndex, before update the key {}, value is {}", key, redisTemplate.opsForValue().get(redisKey));
        AdUnitObject oldObject = (AdUnitObject) redisTemplate.opsForValue().get(redisKey);
        oldObject = (oldObject == null ? value : oldObject.update(value));
        redisTemplate.opsForValue().set(redisKey, oldObject);
        log.info("AdUnitIndex, after update the key {}, value is {}", key, redisTemplate.opsForValue().get(redisKey));
    }

    @Override
    public void delete(Long key, AdUnitObject value) {
        String redisKey = AD_UNIT_INDEX_PREFIX + key;
        log.info("AdUnitIndex, before delete the key set is {}", redisTemplate.keys(AD_UNIT_INDEX_PREFIX + "*"));
        redisTemplate.delete(redisKey);
        log.info("AdUnitIndex, after delete the key set is {}", redisTemplate.keys(AD_UNIT_INDEX_PREFIX + "*"));
    }

    /**
     * 匹配某一流量类型的广告单元，返回单元id集合
     *
     * @param positionType
     * @return
     */
    public Set<Long> match(Integer positionType) {
        Set<Long> adUnitIds = Sets.newHashSet();
        Set<String> matchKeySet = RedisUtil.scan(redisTemplate, AD_UNIT_INDEX_PREFIX + "*", -1);
        matchKeySet.forEach(redisKey -> {
            if (AdUnitObject.isAdSlotTypeOk(positionType, ((AdUnitObject) redisTemplate.opsForValue().get(redisKey)).getPositionType())) {
                adUnitIds.add(Long.valueOf(StringUtils.remove(redisKey, AD_UNIT_INDEX_PREFIX)));
            }
        });
        return adUnitIds;
    }

    /**
     * 根据推广单元ID抓取到指定推广单元
     *
     * @param targetUnitIdSet
     * @return
     */
    public List<AdUnitObject> fetch(Set<Long> targetUnitIdSet) {
        if (CollectionUtils.isEmpty(targetUnitIdSet))
            return Collections.emptyList();
        List<AdUnitObject> adUnitObjectList = Lists.newArrayList();
        targetUnitIdSet.forEach(adUnitId -> {
            AdUnitObject adUnitObject = get(adUnitId);
            if (adUnitObject == null) {
                log.error("redis中未找到对应AdUnitObject, key : {}", AD_UNIT_INDEX_PREFIX + adUnitId);
                return;
            }
            adUnitObjectList.add(adUnitObject);
        });
        return adUnitObjectList;
    }

}
