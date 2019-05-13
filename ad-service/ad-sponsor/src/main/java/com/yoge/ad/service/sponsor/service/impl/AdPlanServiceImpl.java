package com.yoge.ad.service.sponsor.service.impl;

import com.yoge.ad.service.common.exception.AdException;
import com.yoge.ad.service.common.exception.AdPlanException;
import com.yoge.ad.service.common.exception.AdPlanGetException;
import com.yoge.ad.service.sponsor.constant.CommonStatus;
import com.yoge.ad.service.sponsor.constant.Constants;
import com.yoge.ad.service.sponsor.entity.AdPlan;
import com.yoge.ad.service.sponsor.entity.AdUser;
import com.yoge.ad.service.sponsor.repo.AdPlanRepository;
import com.yoge.ad.service.sponsor.repo.AdUserRepository;
import com.yoge.ad.service.sponsor.service.AdPlanService;
import com.yoge.ad.service.sponsor.util.CommonUtils;
import com.yoge.ad.service.sponsor.vo.AdPlanGetRequest;
import com.yoge.ad.service.sponsor.vo.AdPlanRequest;
import com.yoge.ad.service.sponsor.vo.AdPlanResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/12
 */
@Service
@Slf4j
public class AdPlanServiceImpl implements AdPlanService {

    @Autowired
    private AdPlanRepository adPlanRepository;

    @Autowired
    private AdUserRepository adUserRepository;

    @Override
    @Transactional(rollbackFor = {Exception.class, AdPlanException.class})
    public AdPlanResponse createAdPlan(AdPlanRequest adPlanRequest) throws AdException {
        if (!adPlanRequest.createPlanValidate()) {
            throw new AdPlanException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        // 确保存在关联user
        Optional<AdUser> adUser = adUserRepository.findById(adPlanRequest.getUserId());
        if (!adUser.isPresent()) {
            throw new AdPlanException(Constants.ErrorMsg.CANNOT_FOUND_RECORD);
        }
        AdPlan oldPlan = adPlanRepository.findByUserIdAndPlanName(adPlanRequest.getUserId(), adPlanRequest.getPlanName());
        if (oldPlan != null) {
            throw new AdPlanException(Constants.ErrorMsg.SAME_NAME_PLAN_ERROR);
        }
        AdPlan newPlan = adPlanRepository.save(
                new AdPlan(
                        adPlanRequest.getUserId(),
                        adPlanRequest.getPlanName(),
                        CommonUtils.parseString2LocalDateTime(adPlanRequest.getStartDate()),
                        CommonUtils.parseString2LocalDateTime(adPlanRequest.getEndDate())
                )
        );
        return new AdPlanResponse(newPlan.getId(), newPlan.getPlanName());
    }

    @Override
    public List<AdPlan> getAdPlansByIds(AdPlanGetRequest request) throws AdException {
        if (!request.validate()) {
            throw new AdPlanGetException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        return adPlanRepository.findAllByIdInAndUserId(request.getIds(), request.getUserId());
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, AdPlanException.class})
    public AdPlanResponse updateAdPlan(AdPlanRequest request) throws AdException {
        if (!request.updatePlanValidate()) {
            throw new AdPlanException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        AdPlan oldPlan = adPlanRepository.findByIdAndUserId(request.getId(), request.getUserId());
        if (oldPlan == null) {
            throw new AdPlanException(Constants.ErrorMsg.CANNOT_FOUND_RECORD);
        }
        if (StringUtils.isNotBlank(request.getPlanName())) {
            oldPlan.setPlanName(request.getPlanName());
        }
        if (request.getStartDate() != null) {
            oldPlan.setStartDate(CommonUtils.parseString2LocalDateTime(request.getStartDate()));
        }
        if (request.getEndDate() != null) {
            oldPlan.setEndDate(CommonUtils.parseString2LocalDateTime(request.getEndDate()));
        }
        oldPlan.setUpdateTime(LocalDateTime.now());
        oldPlan = adPlanRepository.save(oldPlan);
        return new AdPlanResponse(oldPlan.getId(), oldPlan.getPlanName());
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, AdPlanException.class})
    public void deleteAdPlan(AdPlanRequest request) throws AdException {
        if (!request.deletePlanValidate()) {
            throw new AdPlanException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        AdPlan oldPlan = adPlanRepository.findByIdAndUserId(request.getId(), request.getUserId());
        if (oldPlan == null) {
            throw new AdPlanException(Constants.ErrorMsg.CANNOT_FOUND_RECORD);
        }
        oldPlan.setUpdateTime(LocalDateTime.now());
        oldPlan.setPlanStatus(CommonStatus.INVALID.getStatus());
        adPlanRepository.save(oldPlan);
    }
}
