package com.yoge.ad.service.sponsor.service;

import com.yoge.ad.service.common.exception.AdException;
import com.yoge.ad.service.sponsor.vo.*;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/12
 */
public interface AdUnitService {

    /**
     * 创建新的推广单元
     * @param request
     * @return
     * @throws AdException
     */
    AdUnitResponse createUnit(AdUnitRequest request) throws AdException;

    /**
     * 创建推广单元关键词限制
     * @param request
     * @return
     * @throws AdException
     */
    AdUnitKeywordResponse createUnitKeyWord(AdUnitKeywordRequest request) throws AdException;

    /**
     * 创建推广单元兴趣限制
     * @param request
     * @return
     * @throws AdException
     */
    AdUnitItResponse createUnitIt(AdUnitItRequest request) throws AdException;

    /**
     * 创建推广单元地域限制
     * @param request
     * @return
     * @throws AdException
     */
    AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request) throws AdException;

    /**
     * 创建推广单元创意
     * @param request
     * @return
     * @throws AdException
     */
    CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException;
}
