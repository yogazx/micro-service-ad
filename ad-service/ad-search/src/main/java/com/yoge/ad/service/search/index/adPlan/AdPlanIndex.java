package com.yoge.ad.service.search.index.adPlan;

import com.yoge.ad.service.search.index.IndexAware;
import com.yoge.ad.service.search.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/16
 */
@Slf4j
@Component
public class AdPlanIndex implements IndexAware<Long, AdPlanObject> {

    // 推广计划索引 redis_key 前缀
    private static final String AD_PLAN_INDEX_PREFIX = "ad_plan_index_prefix_";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public AdPlanObject get(Long key) {
        String redisKey = AD_PLAN_INDEX_PREFIX + key;
        return (AdPlanObject) redisTemplate.opsForValue().get(redisKey);
    }

    @Override
    public void add(Long key, AdPlanObject value) {
        String redisKey = AD_PLAN_INDEX_PREFIX + key;
//        log.info("AdPlanIndex, before add the key set is {}", redisTemplate.keys(AD_PLAN_INDEX_PREFIX + "*"));
        log.info("AdPlanIndex, before add the key set is {}", RedisUtil.scan(redisTemplate, AD_PLAN_INDEX_PREFIX + "*", Integer.MAX_VALUE));
        // 如果key不存在则新增，如果存在则不改变已有的值
        redisTemplate.opsForValue().setIfAbsent(redisKey, value);
//        log.info("AdPlanIndex, after add the key set is {}", redisTemplate.keys(AD_PLAN_INDEX_PREFIX + "*"));
        log.info("AdPlanIndex, after add the key set is {}", RedisUtil.scan(redisTemplate, AD_PLAN_INDEX_PREFIX + "*", Integer.MAX_VALUE));
    }

    @Override
    public void update(Long key, AdPlanObject value) {
        String redisKey = AD_PLAN_INDEX_PREFIX + key;
        log.info("AdPlanIndex, before update the key is {}, the value is {}", key, redisTemplate.opsForValue().get(redisKey));
        AdPlanObject oldObject = (AdPlanObject) redisTemplate.opsForValue().get(redisKey);
        oldObject = (oldObject == null ? value : oldObject.update(value));
        redisTemplate.opsForValue().set(redisKey, oldObject);
        log.info("AdPlanIndex, after update the key is {}, the value is {}", key, redisTemplate.opsForValue().get(redisKey));
    }

    @Override
    public void delete(Long key, AdPlanObject value) {
        String redisKey = AD_PLAN_INDEX_PREFIX + key;
//        log.info("AdPlanIndex, before delete the key set is {}", redisTemplate.keys(AD_PLAN_INDEX_PREFIX + "*"));
        log.info("AdPlanIndex, before delete the key set is {}", RedisUtil.scan(redisTemplate, AD_PLAN_INDEX_PREFIX + "*", Integer.MAX_VALUE));
        redisTemplate.delete(redisKey);
//        log.info("AdPlanIndex, after delete the key set is {}", redisTemplate.keys(AD_PLAN_INDEX_PREFIX + "*"));
        log.info("AdPlanIndex, after delete the key set is {}", RedisUtil.scan(redisTemplate, AD_PLAN_INDEX_PREFIX + "*", Integer.MAX_VALUE));
    }
}
