package com.yoge.ad.service.search.controller;

import com.alibaba.fastjson.JSON;
import com.yoge.ad.service.common.annotation.IgnoreResponseAdvice;
import com.yoge.ad.service.common.vo.CommonResponse;
import com.yoge.ad.service.search.client.SponsorClient;
import com.yoge.ad.service.search.client.vo.AdPlan;
import com.yoge.ad.service.search.client.vo.AdPlanGetRequest;
import com.yoge.ad.service.search.mediaSearch.ISearch;
import com.yoge.ad.service.search.mediaSearch.vo.SearchRequest;
import com.yoge.ad.service.search.mediaSearch.vo.SearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/14
 */
@RestController
@Slf4j
public class SearchController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SponsorClient sponsorClient;

    @Autowired
    private ISearch search;

    @PostMapping("/getAdPlanListByRibbon")
    @IgnoreResponseAdvice
    @SuppressWarnings("unchecked")
    public CommonResponse<List<AdPlan>> getAdPlanListByRibbon(@RequestBody AdPlanGetRequest request) {
        log.info("ad-search: getAdPlanListByRibbon -> {}", JSON.toJSONString(request));
        return restTemplate.postForEntity("http://ad-sponsor-client/ad-sponsor/get/adPlan", request, CommonResponse.class).getBody();
    }

    @PostMapping("/getAdPlanListByFeign")
    @IgnoreResponseAdvice
    public CommonResponse<List<AdPlan>> getAdPlanListByFeign(@RequestBody AdPlanGetRequest request) {
        log.info("ad-search: getAdPlanByFeign -> {}", JSON.toJSONString(request));
        return sponsorClient.getAdPlanListByIdList(request);
    }

    @PostMapping("/fetchAds")
    public SearchResponse fetchAds(SearchRequest request) {
        log.info("ad-search: fetch ads -> {}", JSON.toJSONString(request));
        return search.fetchAds(request);
    }
}
