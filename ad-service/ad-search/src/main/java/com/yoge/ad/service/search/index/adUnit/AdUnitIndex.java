package com.yoge.ad.service.search.index.adUnit;

import com.yoge.ad.service.search.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

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
}
