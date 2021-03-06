package com.yoge.ad.service.search.index.creativeUnit;

import com.google.common.collect.Lists;
import com.yoge.ad.service.search.index.IndexAware;
import com.yoge.ad.service.search.index.adUnit.AdUnitObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * DESC 推广创意与推广单元关联索引实现
 *
 * @author You Jia Ge
 * Created Time 2019/5/18
 */
@Slf4j
@Component
public class CreativeUnitIndex implements IndexAware<String, CreativeUnitObject> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 创意id + 推广单元 id ---> creativeUnitObject
     * key: adId + "-" + unitId; value: creative
     * redis --  key: prefix + adId + "-" + unitId; value: creative
     */
    private static final String AD_UNIT_CREATIVE_INDEX_PREFIX = "ad_unit_creative_index_prefix_";

    /**
     * 创意id --> 推广单元id Set
     * key: adId; value: unitIdSet
     * redis --  key: prefix + adId; value: unitIdSet
     */
    private static final String AD_CREATIVE_UNIT_INDEX_PREFIX = "ad_creative_unit_index_prefix_";

    /**
     * 推广单元 id --> 创意id Set
     * key: unitId; value: adIdSet
     * redis --  key: prefix + unitId; value: adIdSet
     */
    private static final String UNIT_AD_INDEX_PREFIX = "unit_ad_index_prefix_";

    @Override
    public CreativeUnitObject get(String key) {
        return (CreativeUnitObject) redisTemplate.opsForValue().get(AD_UNIT_CREATIVE_INDEX_PREFIX + key);
    }

    @Override
    public void add(String key, CreativeUnitObject value) {
        log.info("CreativeUnitIndex, before add the key set is {}", redisTemplate.keys(AD_UNIT_CREATIVE_INDEX_PREFIX + "*"));
        redisTemplate.opsForValue().set(AD_UNIT_CREATIVE_INDEX_PREFIX + key, value);
        redisTemplate.opsForSet().add(AD_CREATIVE_UNIT_INDEX_PREFIX + value.getAdId(), value.getUnitId());
        redisTemplate.opsForSet().add(UNIT_AD_INDEX_PREFIX + value.getUnitId(), value.getAdId());
        log.info("CreativeUnitIndex, after add the key set is {}", redisTemplate.keys(AD_UNIT_CREATIVE_INDEX_PREFIX + "*"));
    }

    // todo 待完善
    @Override
    public void update(String key, CreativeUnitObject value) {
        log.error("CreativeUnitIndex, creativeUnit index update is not supported");
    }

    @Override
    public void delete(String key, CreativeUnitObject value) {
        log.info("CreativeUnitIndex, before delete the key set is {}", redisTemplate.keys(AD_UNIT_CREATIVE_INDEX_PREFIX + "*"));
        redisTemplate.delete(AD_UNIT_CREATIVE_INDEX_PREFIX + key);
        redisTemplate.opsForSet().remove(AD_CREATIVE_UNIT_INDEX_PREFIX + value.getAdId(), value.getUnitId());
        redisTemplate.opsForSet().remove(UNIT_AD_INDEX_PREFIX + value.getUnitId(), value.getAdId());
        log.info("CreativeUnitIndex, after delete the key set is {}", redisTemplate.keys(AD_UNIT_CREATIVE_INDEX_PREFIX + "*"));
    }

    /**
     * 通过推广单元获取到创意对象ID
     *
     * @param adUnitObjectList
     * @return
     */
    public List<Long> getCreativeIdsByAdUnitObject(List<AdUnitObject> adUnitObjectList) {
        if (CollectionUtils.isEmpty(adUnitObjectList))
            return Collections.emptyList();
        List<Long> creativeIdList = Lists.newArrayList();
        adUnitObjectList.forEach(adUnitObject -> {
            Set<Object> creativeIdSet = redisTemplate.boundSetOps(UNIT_AD_INDEX_PREFIX + adUnitObject.getUnitId()).members();
            if (CollectionUtils.isNotEmpty(creativeIdSet)) {
                creativeIdSet.forEach(creativeId -> creativeIdList.add(Long.valueOf(creativeId.toString())));
            }
        });
        return creativeIdList;
    }
}
