package com.yoge.ad.service.search.index.interest;

import com.yoge.ad.service.search.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * DESC 兴趣索引对象实现
 *
 * @author You Jia Ge
 * Created Time 2019/5/17
 */
@Slf4j
@Component
public class UnitItIndex implements IndexAware<String, Set<Long>> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 倒排索引
     * key: itTag (String) ; value: adUnitIdSet (Set<long>)
     * redis --  key : prefix + itTag , value : adUnitIdSet
     */
    private static final String IT_UNIT_INDEX_PREFIX = "it_unit_index_prefix_";

    /**
     * 正向索引
     * key: adUnitId (Long) ; value: itTagSet (Set<String>)
     * redis --  key : prefix + adUnitId , value : itTagSet
     */
    private static final String UNIT_IT_INDEX_PREFIX = "unit_it_index_prefix_";

    @Override
    public Set<Long> get(String key) {
        return (Set) redisTemplate.opsForSet().members(IT_UNIT_INDEX_PREFIX + key);
    }

    @Override
    public void add(String key, Set<Long> values) {
        log.info("UnitItIndex, before add the key set is {}", redisTemplate.keys(IT_UNIT_INDEX_PREFIX + "*"));
        redisTemplate.opsForSet().add(IT_UNIT_INDEX_PREFIX + key, values.toArray());
        for (Long unitId : values) {
            redisTemplate.opsForSet().add(UNIT_IT_INDEX_PREFIX + unitId, key);
        }
        log.info("UnitItIndex, after add the key set is {}", redisTemplate.keys(IT_UNIT_INDEX_PREFIX + "*"));
    }

    // todo 待完善
    @Override
    public void update(String key, Set<Long> value) {
        log.error("UnitItIndex, it index update is not supported");
    }

    @Override
    public void delete(String key, Set<Long> value) {
        log.info("UnitItIndex, before delete the key set is {}", redisTemplate.keys(IT_UNIT_INDEX_PREFIX + "*"));
        redisTemplate.opsForSet().remove(IT_UNIT_INDEX_PREFIX + key, value.toArray());
        for (Long unitId : value) {
            redisTemplate.opsForSet().remove(UNIT_IT_INDEX_PREFIX + unitId, key);
        }
        log.info("UnitItIndex, after delete the key set is {}", redisTemplate.keys(IT_UNIT_INDEX_PREFIX + "*"));
    }
}
