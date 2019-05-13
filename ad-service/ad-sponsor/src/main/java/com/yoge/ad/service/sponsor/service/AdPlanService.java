package com.yoge.ad.service.sponsor.service;

import com.yoge.ad.service.common.exception.AdException;
import com.yoge.ad.service.sponsor.entity.AdPlan;
import com.yoge.ad.service.sponsor.vo.AdPlanGetRequest;
import com.yoge.ad.service.sponsor.vo.AdPlanRequest;
import com.yoge.ad.service.sponsor.vo.AdPlanResponse;

import java.util.List;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/12
 */
public interface AdPlanService {

    /**
     * 创建推广计划
     * @param request
     * @return
     * @throws AdException
     */
    AdPlanResponse createAdPlan(AdPlanRequest request) throws AdException;

    /**
     * 获取推广计划
     * @param request
     * @return
     * @throws AdException
     */
    List<AdPlan> getAdPlansByIds(AdPlanGetRequest request) throws AdException;

    /**
     * 更新推广计划
     * @param request
     * @return
     * @throws AdException
     */
    AdPlanResponse updateAdPlan(AdPlanRequest request) throws AdException;

    /**
     * 删除推广计划
     * @param request
     * @throws AdException
     */
    void deleteAdPlan(AdPlanRequest request) throws AdException;
}
