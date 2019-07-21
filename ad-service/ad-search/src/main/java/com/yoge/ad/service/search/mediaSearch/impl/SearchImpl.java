package com.yoge.ad.service.search.mediaSearch.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.yoge.ad.service.search.index.CommonStatus;
import com.yoge.ad.service.search.index.DataTable;
import com.yoge.ad.service.search.index.District.UnitDistrictIndex;
import com.yoge.ad.service.search.index.adUnit.AdUnitIndex;
import com.yoge.ad.service.search.index.adUnit.AdUnitObject;
import com.yoge.ad.service.search.index.creative.CreativeIndex;
import com.yoge.ad.service.search.index.creative.CreativeObject;
import com.yoge.ad.service.search.index.creativeUnit.CreativeUnitIndex;
import com.yoge.ad.service.search.index.interest.UnitItIndex;
import com.yoge.ad.service.search.index.unitKeyword.UnitKeywordIndex;
import com.yoge.ad.service.search.mediaSearch.ISearch;
import com.yoge.ad.service.search.mediaSearch.vo.SearchRequest;
import com.yoge.ad.service.search.mediaSearch.vo.SearchResponse;
import com.yoge.ad.service.search.mediaSearch.vo.feature.DistrictFeature;
import com.yoge.ad.service.search.mediaSearch.vo.feature.FeatureRelation;
import com.yoge.ad.service.search.mediaSearch.vo.feature.ItFeature;
import com.yoge.ad.service.search.mediaSearch.vo.feature.KeywordFeature;
import com.yoge.ad.service.search.mediaSearch.vo.media.AdSlot;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author geyoujia
 * @date 2019/07/20
 */
@Slf4j
@Service
public class SearchImpl implements ISearch {

    @Override
    public SearchResponse fetchAds(SearchRequest searchRequest) {
        // 请求的广告位信息
        List<AdSlot> adSlotList = searchRequest.getRequestInfo().getAdSlots();
        // 三个广告特征
        KeywordFeature keywordFeature = searchRequest.getFeatureInfo().getKeywordFeature();
        DistrictFeature districtFeature = searchRequest.getFeatureInfo().getDistrictFeature();
        ItFeature itFeature = searchRequest.getFeatureInfo().getItFeature();
        // 特征之间的结合关系
        FeatureRelation featureRelation = searchRequest.getFeatureInfo().getFeatureRelation();

        SearchResponse searchResponse = new SearchResponse();
        Map<String, List<SearchResponse.Creative>> adSlot2Ads = searchResponse.getAdSlot2Ads();
        adSlotList.forEach(adSlot -> {
            Set<Long> targetUnitIdSet;
            Set<Long> adUnitIdSet = DataTable.of(AdUnitIndex.class).match(adSlot.getPositionType());
            // 对adUnitIdSet进行过滤，留下同时满足三个feature的unitId集合
            if (featureRelation.equals(FeatureRelation.AND)) {
                filterKeyWordFeature(adUnitIdSet, keywordFeature);
                filterDistrictFeature(adUnitIdSet, districtFeature);
                filterItFeature(adUnitIdSet, itFeature);
                targetUnitIdSet = adUnitIdSet;
            } else {
                targetUnitIdSet = getOrRelationUnitIds(adUnitIdSet, keywordFeature, districtFeature, itFeature);
            }

            // 通过推广单元ID获取到推广单元
            List<AdUnitObject> adUnitObjectList = DataTable.of(AdUnitIndex.class).fetch(targetUnitIdSet);
            // 通过推广单元状态进行推广单元过滤
            filterAdUnitObjectByAdPlanStatus(adUnitObjectList, CommonStatus.VALID);
            // 通过推广单元获取到创意对象ID
            List<Long> creativeIdList = DataTable.of(CreativeUnitIndex.class).getCreativeIdsByAdUnitObject(adUnitObjectList);
            // 通过创意ID获取创意对象
            List<CreativeObject> creativeObjectList = DataTable.of(CreativeIndex.class).fetch(creativeIdList);

            // 通过adSlot实现对创意对象进行过滤
            filterCreativeByAdSlot(creativeObjectList, adSlot.getHeight(), adSlot.getWidth(), adSlot.getType());
            adSlot2Ads.put(adSlot.getAdSlotCode(), buildCreativeResponse(creativeObjectList));
        });
        log.info("fetch ads : request : {} --  response : {}", JSON.toJSONString(searchRequest), JSON.toJSONString(searchResponse));
        return searchResponse;
    }

    private List<SearchResponse.Creative> buildCreativeResponse(List<CreativeObject> creativeObjectList) {
        if (CollectionUtils.isEmpty(creativeObjectList))
            return Collections.emptyList();
        CreativeObject object = creativeObjectList.get(Math.abs(ThreadLocalRandom.current().nextInt()) % creativeObjectList.size());
        return Collections.singletonList(SearchResponse.convert(object));
    }

    /**
     * 根据广告位信息实施过滤
     *
     * @param creativeObjectList
     * @param height
     * @param width
     * @param typeList
     */
    private void filterCreativeByAdSlot(List<CreativeObject> creativeObjectList, Integer height, Integer width, List<Integer> typeList) {
        if (CollectionUtils.isEmpty(creativeObjectList))
            return;
        CollectionUtils.filter(creativeObjectList, creativeObject ->
            creativeObject.getAuditStatus().equals(CommonStatus.VALID.getStatus()) &&
                    creativeObject.getWidth().equals(width) &&
                    creativeObject.getHeight().equals(height) &&
                    typeList.contains(creativeObject.getType())
        );
    }

    /**
     * 推广单元状态过滤
     *
     * @param adUnitObjectList
     * @param commonStatus
     */
    private void filterAdUnitObjectByAdPlanStatus(List<AdUnitObject> adUnitObjectList, CommonStatus commonStatus) {
        if (CollectionUtils.isEmpty(adUnitObjectList))
            return;
        CollectionUtils.filter(adUnitObjectList, adUnitObject ->
                adUnitObject.getUnitStatus().equals(commonStatus.getStatus()) && adUnitObject.getAdPlanObject().getPlanStatus().equals(commonStatus.getStatus()));
    }

    /**
     * 针对 OR 关联关系进行过滤
     *
     * @param adUnitIdSet
     * @param keywordFeature
     * @param districtFeature
     * @param itFeature
     * @return
     */
    private Set<Long> getOrRelationUnitIds(Set<Long> adUnitIdSet, KeywordFeature keywordFeature, DistrictFeature districtFeature, ItFeature itFeature) {
        if (CollectionUtils.isEmpty(adUnitIdSet))
            return Collections.emptySet();
        Set<Long> keywordUnitIds = Sets.newHashSet(adUnitIdSet);
        Set<Long> districtUnitIds = Sets.newHashSet(adUnitIdSet);
        Set<Long> itUnitIds = Sets.newHashSet(adUnitIdSet);
        filterKeyWordFeature(keywordUnitIds, keywordFeature);
        filterDistrictFeature(districtUnitIds, districtFeature);
        filterItFeature(itUnitIds, itFeature);
        return Sets.newHashSet(CollectionUtils.union(CollectionUtils.union(keywordUnitIds, districtUnitIds), itUnitIds));
    }

    /**
     * 针对 AND 关联关系进行关键词匹配过滤
     *
     * @param adUnitIdList
     * @param keywordFeature
     */
    private void filterKeyWordFeature(Collection<Long> adUnitIdList, KeywordFeature keywordFeature) {
        if (CollectionUtils.isEmpty(adUnitIdList))
            return;
        if (CollectionUtils.isNotEmpty(keywordFeature.getKeywords())) {
            CollectionUtils.filter(adUnitIdList, adUnitId -> DataTable.of(UnitKeywordIndex.class).match(adUnitId, keywordFeature.getKeywords()));
        }
    }

    /**
     * 针对 AND 关联关系进行地域维度过滤
     *
     * @param adUnitIdList ID集合
     * @param districtFeature 地域限制
     */
    private void filterDistrictFeature(Collection<Long> adUnitIdList, DistrictFeature districtFeature) {
        if (CollectionUtils.isEmpty(adUnitIdList))
            return;
        if (CollectionUtils.isNotEmpty(districtFeature.getDistricts())) {
            CollectionUtils.filter(adUnitIdList, adUnitId -> DataTable.of(UnitDistrictIndex.class).match(adUnitId, districtFeature.getDistricts()));
        }
    }

    /**
     * 针对 AND 关联关系进行兴趣匹配过滤
     *
     * @param adUnitIdList
     * @param itFeature
     */
    private void filterItFeature(Collection<Long> adUnitIdList, ItFeature itFeature) {
        if (CollectionUtils.isEmpty(adUnitIdList))
            return;
        if (CollectionUtils.isNotEmpty(itFeature.getIts())) {
            CollectionUtils.filter(adUnitIdList, adUnitId -> DataTable.of(UnitItIndex.class).match(adUnitId, itFeature.getIts()));
        }
    }
}
