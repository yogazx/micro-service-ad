package com.yoge.ad.service.sponsor.service.impl;

import com.yoge.ad.service.common.exception.AdUserException;
import com.yoge.ad.service.sponsor.constant.Constants;
import com.yoge.ad.service.sponsor.entity.AdUser;
import com.yoge.ad.service.sponsor.repo.AdUserRepository;
import com.yoge.ad.service.sponsor.service.UserService;
import com.yoge.ad.service.sponsor.util.CommonUtils;
import com.yoge.ad.service.sponsor.vo.CreateUserRequest;
import com.yoge.ad.service.sponsor.vo.CreateUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/12
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AdUserRepository adUserRepository;

    @Override
    @Transactional(rollbackFor = {Exception.class, AdUserException.class})
    public CreateUserResponse createUser(CreateUserRequest request) throws AdUserException {
        if (!request.validate()) {
            throw new AdUserException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        AdUser oldUser = adUserRepository.findAdUserByUsername(request.getUsername());
        if (oldUser != null) {
            throw new AdUserException(Constants.ErrorMsg.SAME_NAME_USER_ERROR);
        }
        AdUser newUser = adUserRepository.save(
                new AdUser(
                        request.getUsername(),
                        CommonUtils.md5(request.getUsername())
                )
        );
        return new CreateUserResponse(
                newUser.getId(),
                newUser.getUsername(),
                newUser.getToken(),
                newUser.getCreateTime(),
                newUser.getUpdateTime()
        );
    }
}
