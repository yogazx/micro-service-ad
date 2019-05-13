package com.yoge.ad.service.sponsor.service;

import com.yoge.ad.service.common.exception.AdUserException;
import com.yoge.ad.service.sponsor.vo.CreateUserRequest;
import com.yoge.ad.service.sponsor.vo.CreateUserResponse;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/12
 */
public interface UserService {

    /**
     * 创建用户
     * @param request
     * @return
     */
    CreateUserResponse createUser(CreateUserRequest request) throws AdUserException;
}
