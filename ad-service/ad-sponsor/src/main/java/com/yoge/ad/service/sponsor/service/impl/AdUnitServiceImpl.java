package com.yoge.ad.service.sponsor.service.impl;

import com.google.common.collect.Lists;
import com.yoge.ad.service.common.exception.*;
import com.yoge.ad.service.sponsor.constant.Constants;
import com.yoge.ad.service.sponsor.entity.AdPlan;
import com.yoge.ad.service.sponsor.entity.AdUnit;
import com.yoge.ad.service.sponsor.entity.unitcondition.AdUnitDistrict;
import com.yoge.ad.service.sponsor.entity.unitcondition.AdUnitIt;
import com.yoge.ad.service.sponsor.entity.unitcondition.AdUnitKeyword;
import com.yoge.ad.service.sponsor.entity.unitcondition.CreativeUnit;
import com.yoge.ad.service.sponsor.repo.AdPlanRepository;
import com.yoge.ad.service.sponsor.repo.AdUnitRepository;
import com.yoge.ad.service.sponsor.repo.unitcondition.AdUnitDistrictRepository;
import com.yoge.ad.service.sponsor.repo.unitcondition.AdUnitItRepository;
import com.yoge.ad.service.sponsor.repo.unitcondition.AdUnitKeywordRepository;
import com.yoge.ad.service.sponsor.repo.unitcondition.CreativeUnitRepository;
import com.yoge.ad.service.sponsor.service.AdUnitService;
import com.yoge.ad.service.sponsor.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/12
 */
@Slf4j
@Service
public class AdUnitServiceImpl implements AdUnitService {

    @Autowired
    private AdUnitRepository adUnitRepository;

    @Autowired
    private AdPlanRepository adPlanRepository;

    @Autowired
    private AdUnitKeywordRepository adUnitKeywordRepository;

    @Autowired
    private AdUnitItRepository adUnitItRepository;

    @Autowired
    private AdUnitDistrictRepository adUnitDistrictRepository;

    @Autowired
    private CreativeUnitRepository creativeUnitRepository;

    @Override
    @Transactional(rollbackFor = {Exception.class, AdUnitException.class})
    public AdUnitResponse createUnit(AdUnitRequest request) throws AdException {
        if (!request.createValidate()) {
            throw new AdUnitException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        // 查找关联推广计划
        Optional<AdPlan> adPlan = adPlanRepository.findById(request.getPlanId());
        if (!adPlan.isPresent()) {
            throw new AdUnitException(Constants.ErrorMsg.CANNOT_FOUND_RECORD);
        }
        Optional<AdUnit> adUnit = adUnitRepository.findByPlanIdAndUnitName(request.getPlanId(), request.getUnitName());
        if (adUnit.isPresent()) {
            throw new AdUnitException(Constants.ErrorMsg.SAME_NAME_UNIT_ERROR);
        }
        AdUnit newUnit = adUnitRepository.save(
                new AdUnit(
                        request.getPlanId(),
                        request.getUnitName(),
                        request.getPositionType(),
                        request.getBudget()
                )
        );
        return new AdUnitResponse(newUnit.getId(), newUnit.getUnitName());
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, AdUnitKeywordException.class})
    public AdUnitKeywordResponse createUnitKeyWord(AdUnitKeywordRequest request) throws AdException {
        List<Long> unitIdList = request.getUnitKeywordList()
                .stream()
                .map(AdUnitKeywordRequest.UnitKeyword::getUnitId)
                .distinct()
                .collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIdList)) {
            throw new AdUnitKeywordException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        List<Long> idList = Collections.emptyList();
        List<AdUnitKeyword> unitKeywordList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(request.getUnitKeywordList())) {
            request.getUnitKeywordList().forEach(unitKeyword -> unitKeywordList.add(new AdUnitKeyword(unitKeyword.getUnitId(), unitKeyword.getKeyword())));
            idList = adUnitKeywordRepository.saveAll(unitKeywordList)
                    .stream()
                    .map(AdUnitKeyword::getId)
                    .collect(Collectors.toList());
        }
        return new AdUnitKeywordResponse(idList);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, AdUnitItException.class})
    public AdUnitItResponse createUnitIt(AdUnitItRequest request) throws AdException {
        List<Long> unitIdList = request.getUnitItList()
                .stream()
                .map(AdUnitItRequest.UnitIt::getUnitId)
                .distinct()
                .collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIdList)) {
            throw new AdUnitItException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        List<Long> idList = Lists.newArrayList();
        List<AdUnitIt> adUnitItList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(request.getUnitItList())) {
            request.getUnitItList().forEach(unitIt -> adUnitItList.add(new AdUnitIt(unitIt.getUnitId(), unitIt.getItTag())));
            idList = adUnitItRepository.saveAll(adUnitItList)
                    .stream()
                    .map(AdUnitIt::getId)
                    .collect(Collectors.toList());
        }
        return new AdUnitItResponse(idList);
    }

    @Override
    public AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request) throws AdException {
        List<Long> unitIdList = request.getUnitDistrictList()
                .stream()
                .map(AdUnitDistrictRequest.UnitDistrict::getUnitId)
                .distinct()
                .collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIdList)) {
            throw new AdUnitDistrictException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        List<Long> idList = Lists.newArrayList();
        List<AdUnitDistrict> unitDistrictList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(request.getUnitDistrictList())) {
            request.getUnitDistrictList().forEach(unitDistrict -> unitDistrictList.add(new AdUnitDistrict(unitDistrict.getUnitId(), unitDistrict.getProvince(), unitDistrict.getCity())));
            idList = adUnitDistrictRepository.saveAll(unitDistrictList)
                    .stream()
                    .map(AdUnitDistrict::getId)
                    .collect(Collectors.toList());
        }
        return new AdUnitDistrictResponse(idList);
    }

    @Override
    public CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException {
        List<Long> unitIdList = request.getCreativeUnitItemList()
                .stream()
                .map(CreativeUnitRequest.CreativeUnitItem::getUnitId)
                .distinct()
                .collect(Collectors.toList());
        List<Long> creativeIdList = request.getCreativeUnitItemList()
                .stream()
                .map(CreativeUnitRequest.CreativeUnitItem::getCreativeId)
                .distinct()
                .collect(Collectors.toList());
        if (!(isRelatedUnitExist(unitIdList) && isRelatedCreativeExist(creativeIdList))) {
            throw new CreativeUnitException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        List<Long> idList = Lists.newArrayList();
        List<CreativeUnit> creativeUnitList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(request.getCreativeUnitItemList())) {
            request.getCreativeUnitItemList().forEach(creativeUnitItem -> creativeUnitList.add(new CreativeUnit(creativeUnitItem.getCreativeId(), creativeUnitItem.getUnitId())));
            idList = creativeUnitRepository.saveAll(creativeUnitList)
                    .stream()
                    .map(CreativeUnit::getId)
                    .collect(Collectors.toList());
        }
        return new CreativeUnitResponse(idList);
    }

    /**
     * 判断当前unitIdList是否存在
     *
     * @param unitIdList
     * @return
     */
    private boolean isRelatedUnitExist(List<Long> unitIdList) {
        if (CollectionUtils.isEmpty(unitIdList)) {
            return false;
        }
        // 这里要对unitIdList是已经去重后的
        return adUnitRepository.findAllById(unitIdList).size() == unitIdList.size();
    }

    /**
     * 判断当前creativeIdList是否存在
     *
     * @param creativeIdList
     * @return
     */
    private boolean isRelatedCreativeExist(List<Long> creativeIdList) {
        if (CollectionUtils.isEmpty(creativeIdList)) {
            return false;
        }
        // 这里要对creativeIdList是已经去重后的
        return creativeUnitRepository.findAllById(creativeIdList).size() == creativeIdList.size();
    }
}
