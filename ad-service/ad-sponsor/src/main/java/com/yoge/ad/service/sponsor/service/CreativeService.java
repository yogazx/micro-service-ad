package com.yoge.ad.service.sponsor.service;

import com.yoge.ad.service.common.exception.AdException;
import com.yoge.ad.service.sponsor.vo.CreativeRequest;
import com.yoge.ad.service.sponsor.vo.CreativeResponse;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/13
 */
public interface CreativeService {

    /**
     * 创建推广创意
     * @param request
     * @return
     * @throws AdException
     */
    CreativeResponse createCreative(CreativeRequest request) throws AdException;
}
