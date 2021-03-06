package com.yoge.ad.service.search.index.District;

import com.google.common.collect.Sets;
import com.yoge.ad.service.search.index.IndexAware;
import com.yoge.ad.service.search.mediaSearch.vo.feature.DistrictFeature;
import com.yoge.ad.service.search.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DESC 地域索引对象实现
 *
 * @author You Jia Ge
 * Created Time 2019/5/17
 */
@Slf4j
@Component
public class UnitDistrictIndex implements IndexAware<String, Set<Long>> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 倒排索引 <p>
     * 地域(省市) --> 推广单元
     * key: district(province + "-" + city); value: unitIdSet
     * redis  --  key: prefix + province + "-" + city; value: unitIdSet
     */
    private static final String DISTRICT_UNIT_INDEX_PREFIX = "district_unit_index_prefix_";

    /**
     * 正向索引 <p></>
     * 推广单元 --> 地域(省市)
     * key: unitId --> district(province + "-" + city)
     * reids  --  key: prefix + unitId; value: districtSet(prefix + province + "-" + city)
     */
    private static final String UNIT_DISTRICT_INDEX_PREFIX = "unit_district_index_prefix_";

    @Override
    public Set<Long> get(String key) {
        return (Set) redisTemplate.opsForSet().members(DISTRICT_UNIT_INDEX_PREFIX + key);
    }

    @Override
    public void add(String key, Set<Long> value) {
        log.info("UnitDistrictIndex, before add the key set is {}", redisTemplate.keys(DISTRICT_UNIT_INDEX_PREFIX + "*"));
        redisTemplate.opsForSet().add(DISTRICT_UNIT_INDEX_PREFIX + key, value.toArray());
        for (Long unitId : value) {
            redisTemplate.opsForSet().add(UNIT_DISTRICT_INDEX_PREFIX + unitId, key);
        }
        log.info("UnitDistrictIndex, after add the key set is {}", redisTemplate.keys(DISTRICT_UNIT_INDEX_PREFIX + "*"));
    }

    // todo 待完成
    @Override
    public void update(String key, Set<Long> value) {
        log.error("UnitDistrictIndex, district index update is not supported");
    }

    @Override
    public void delete(String key, Set<Long> value) {
        log.info("UnitDistrictIndex, before delete the key set is {}", redisTemplate.keys(DISTRICT_UNIT_INDEX_PREFIX + "*"));
        redisTemplate.opsForSet().remove(DISTRICT_UNIT_INDEX_PREFIX + key, value.toArray());
        for (Long unitId : value) {
            redisTemplate.opsForSet().remove(UNIT_DISTRICT_INDEX_PREFIX + unitId, key);
        }
        log.info("UnitDistrictIndex, after delete the key set is {}", redisTemplate.keys(DISTRICT_UNIT_INDEX_PREFIX + "*"));
    }

    public boolean match(Long adUnitId, List<DistrictFeature.ProvinceAndCity> districts) {
        if (redisTemplate.hasKey(UNIT_DISTRICT_INDEX_PREFIX + adUnitId) &&
                CollectionUtils.isNotEmpty(redisTemplate.opsForSet().members(UNIT_DISTRICT_INDEX_PREFIX + adUnitId))) {
            List<String> targetDistrict = districts.stream()
                    .map(district -> CommonUtils.stringContact(district.getProvince(), district.getCity()))
                    .collect(Collectors.toList());
            return CollectionUtils.isSubCollection(targetDistrict, districts);
        }
        return false;
    }
}
