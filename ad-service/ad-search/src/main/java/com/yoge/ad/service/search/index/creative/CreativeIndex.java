package com.yoge.ad.service.search.index.creative;

import com.yoge.ad.service.search.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * DESC 创意索引实现
 *
 * @author You Jia Ge
 * Created Time 2019/5/17
 */
@Slf4j
@Component
public class CreativeIndex implements IndexAware<Long, CreativeObject> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 广告id --> 创意
     * redis --  key : prefix + adId, value : creative
     */
    private static final String AD_CREATIVE_INDEX_PREFIX = "ad_creative_index_prefix_";

    @Override
    public CreativeObject get(Long key) {
        return (CreativeObject) redisTemplate.opsForValue().get(AD_CREATIVE_INDEX_PREFIX + key);
    }

    @Override
    public void add(Long key, CreativeObject value) {
        log.info("CreativeIndex, before add the key set is {}", redisTemplate.keys(AD_CREATIVE_INDEX_PREFIX + "*"));
        redisTemplate.opsForValue().set(AD_CREATIVE_INDEX_PREFIX + key, value);
        log.info("CreativeIndex, after add the key set is {}", redisTemplate.keys(AD_CREATIVE_INDEX_PREFIX + "*"));
    }

    @Override
    public void update(Long key, CreativeObject value) {
        log.info("CreativeIndex, before update the key set is {}", redisTemplate.keys(AD_CREATIVE_INDEX_PREFIX + "*"));
        CreativeObject oldCreative = (CreativeObject) redisTemplate.opsForValue().get(AD_CREATIVE_INDEX_PREFIX + key);
        oldCreative = (oldCreative == null ? value : oldCreative.update(value));
        redisTemplate.opsForValue().set(AD_CREATIVE_INDEX_PREFIX + key, oldCreative);
        log.info("CreativeIndex, after update the key set is {}", redisTemplate.keys(AD_CREATIVE_INDEX_PREFIX + "*"));
    }

    @Override
    public void delete(Long key, CreativeObject value) {
        log.info("CreativeIndex, before delete the key set is {}", redisTemplate.keys(AD_CREATIVE_INDEX_PREFIX + "*"));
        redisTemplate.delete(AD_CREATIVE_INDEX_PREFIX + key);
        log.info("CreativeIndex, after delete the key set is {}", redisTemplate.keys(AD_CREATIVE_INDEX_PREFIX + "*"));
    }
}
