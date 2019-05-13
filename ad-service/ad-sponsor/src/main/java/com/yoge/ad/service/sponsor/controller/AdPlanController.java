package com.yoge.ad.service.sponsor.controller;

import com.alibaba.fastjson.JSON;
import com.yoge.ad.service.common.exception.AdException;
import com.yoge.ad.service.sponsor.entity.AdPlan;
import com.yoge.ad.service.sponsor.service.AdPlanService;
import com.yoge.ad.service.sponsor.vo.AdPlanGetRequest;
import com.yoge.ad.service.sponsor.vo.AdPlanRequest;
import com.yoge.ad.service.sponsor.vo.AdPlanResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/13
 */
@RestController
@Slf4j
public class AdPlanController {

    @Autowired
    private AdPlanService adPlanService;

    @PostMapping("/create/adPlan")
    public AdPlanResponse createAdPlan(@RequestBody AdPlanRequest request) throws AdException{
        log.info("ad-sponsor: createAdPlan -> {}", JSON.toJSONString(request));
        return adPlanService.createAdPlan(request);
    }

    @PostMapping("/get/adPlan")
    public List<AdPlan> getAdPlanByIdList(@RequestBody AdPlanGetRequest request) throws AdException {
        log.info("ad-sponsor: getAdPlanByIdList -> {}", JSON.toJSONString(request));
        return adPlanService.getAdPlansByIds(request);
    }

    @PutMapping("/update/adPlan")
    public AdPlanResponse updateAdPlan(@RequestBody AdPlanRequest request) throws AdException {
        log.info("ad-sponsor: updateAdPlan -> {}", JSON.toJSONString(request));
        return adPlanService.updateAdPlan(request);
    }

    @DeleteMapping("/delete/adPlan")
    public void deleteAdPlan(@RequestBody AdPlanRequest request) throws AdException {
        log.info("ad-sponsor: deleteAdPlan -> {}", JSON.toJSONString(request));
        adPlanService.deleteAdPlan(request);
    }
}
