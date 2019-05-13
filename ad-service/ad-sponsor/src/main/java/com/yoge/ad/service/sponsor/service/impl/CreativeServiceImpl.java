package com.yoge.ad.service.sponsor.service.impl;

import com.yoge.ad.service.common.exception.AdException;
import com.yoge.ad.service.common.exception.CreativeException;
import com.yoge.ad.service.sponsor.constant.Constants;
import com.yoge.ad.service.sponsor.entity.AdUser;
import com.yoge.ad.service.sponsor.entity.Creative;
import com.yoge.ad.service.sponsor.repo.AdCreativeRepository;
import com.yoge.ad.service.sponsor.repo.AdUserRepository;
import com.yoge.ad.service.sponsor.service.CreativeService;
import com.yoge.ad.service.sponsor.vo.CreativeRequest;
import com.yoge.ad.service.sponsor.vo.CreativeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/13
 */
@Service
@Slf4j
public class CreativeServiceImpl implements CreativeService {

    @Autowired
    private AdUserRepository adUserRepository;

    @Autowired
    private AdCreativeRepository adCreativeRepository;

    @Override
    public CreativeResponse createCreative(CreativeRequest request) throws AdException {
        if (!request.isValidate()) {
            throw new CreativeException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        Optional<AdUser> adUser = adUserRepository.findById(request.getUserId());
        if (!adUser.isPresent()) {
            throw new CreativeException(Constants.ErrorMsg.CANNOT_FOUND_RECORD);
        }
        Creative creative = adCreativeRepository.save(request.convertToEntity());
        return new CreativeResponse(creative.getId(), creative.getName());
    }
}
