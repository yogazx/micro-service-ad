package com.yoge.ad.service.search.index.unitKeyword;

import com.google.common.collect.Sets;
import com.yoge.ad.service.search.index.IndexAware;
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
 * DESC 关键词索引对象的实现
 * <p>
 * 考虑到这种推广单元的限制维度, 是以关键词的形式去寻找推广单元的id, 应该使用倒排索引
 * 一个关键词可以对应到多个推广单元id上面
 *
 * @author You Jia Ge
 * Created Time 2019/5/16
 */
@Slf4j
@Component
public class UnitKeywordIndex implements IndexAware<String, Set<Long>> {

    /**
     * keyword --> unit 索引  redis_key 前缀       倒排索引
     * <p>
     * redisKey :  keyword , value : UnitIdSet
     */
    private static final String KEYWORD_UNIT_INDEX_PREFIX = "keyword_unit_index_prefix_";

    /**
     * unit --> keyword 索引  redis_key 前缀       正向索引
     * <p>
     * unitId ---> keywordSet
     */
    private static final String UNIT_KEYWORD_INDEX_PREFIX = "unit_keyword_index_prefix_";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 通过关键词(keyword 推广单元支持的关键词)
     * 获取推广单元的id set
     *
     * @param key keyword
     * @return
     */
    @Override
    public Set<Long> get(String key) {
        Set<Long> unitIdSetResult = Sets.newHashSet();
        if (!StringUtils.isNotBlank(key))
            return Collections.emptySet();
        String redisKey = KEYWORD_UNIT_INDEX_PREFIX + key;
        Set<Object> unitIdSet = redisTemplate.opsForSet().members(redisKey);
        if (unitIdSet == null)
            return Collections.emptySet();
        unitIdSet.forEach(unitId -> unitIdSetResult.add(Long.valueOf(unitId.toString())));
        return unitIdSetResult;
    }

    /**
     * 新增关键词
     *
     * @param key
     * @param values
     */
    @Override
    public void add(String key, Set<Long> values) {
        // key: keyword; value: unitIdSet
        String keywordUnitRedisKey = KEYWORD_UNIT_INDEX_PREFIX + key;
        log.info("UnitKeywordIndex, before add the key set is {}", redisTemplate.keys(UNIT_KEYWORD_INDEX_PREFIX + "*"));
        // key: prefix + keyword; value: Set<Long> unitIdSet
        redisTemplate.opsForSet().add(keywordUnitRedisKey, values.toArray());
        for (Long unitId : values) {
            redisTemplate.opsForSet().add(UNIT_KEYWORD_INDEX_PREFIX + unitId, key);
        }
        log.info("UnitKeywordIndex, after add the key set is {}", redisTemplate.keys(UNIT_KEYWORD_INDEX_PREFIX + "*"));
    }

    /**
     * 因为两个索引对应相互关联  每一个索引的任何一个key 都会对应到一个 set
     * 所以更新的成本会 很高, 因为需要对 set进行遍历,
     * 所以此处 不支持更新
     * todo 待完善
     * @param key
     * @param value
     */
    @Override
    public void update(String key, Set<Long> value) {
        log.error("keyword index update is not supported ");
    }

    /**
     * 因为此处的key keyword可能是一部分unitIdList, 并不是所有的
     * 所以不能直接就将其删除掉, 可能只是删除掉一部分 keyword 到 unitIds的映射
     *
     * @param key
     * @param values
     */
    @Override
    public void delete(String key, Set<Long> values) {
        log.info("UnitKeywordIndex, before delete the key set is {}", redisTemplate.keys(KEYWORD_UNIT_INDEX_PREFIX + "*"));
        redisTemplate.opsForSet().remove(KEYWORD_UNIT_INDEX_PREFIX + key, values.toArray());
        for (Long unitId : values) {
//            redisTemplate.opsForSet().remove(UNIT_KEYWORD_INDEX_PREFIX + unitId, key);
            // 优化
            redisTemplate.boundSetOps(UNIT_KEYWORD_INDEX_PREFIX + unitId).remove(key);
        }
        log.info("UnitKeywordIndex, after delete the key set is {}", redisTemplate.keys(KEYWORD_UNIT_INDEX_PREFIX + "*"));
    }

    /**
     * 用于匹配某个推广单元unitId 是否包含某些关键词List<String> keywords
     *
     * @param unitId
     * @param keywords
     * @return
     */
    public boolean match(Long unitId, List<String> keywords) {
        if (redisTemplate.hasKey(UNIT_KEYWORD_INDEX_PREFIX + unitId)) {
//            Set<Object> unitKeyWords  =redisTemplate.opsForSet().members(UNIT_KEYWORD_INDEX_PREFIX + unitId);
            // 相比上面取出所有set，这里在初始时就绑定一个key
            Set<Object> unitKeyWords = redisTemplate.boundSetOps(UNIT_KEYWORD_INDEX_PREFIX + unitId).members();
            if (CollectionUtils.isNotEmpty(unitKeyWords)) {
                return CollectionUtils.isSubCollection(keywords, unitKeyWords);
            }
        }
        return false;
    }
}
