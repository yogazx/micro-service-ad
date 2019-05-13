package com.yoge.ad.service.sponsor.controller;

import com.alibaba.fastjson.JSON;
import com.yoge.ad.service.common.exception.AdException;
import com.yoge.ad.service.sponsor.service.UserService;
import com.yoge.ad.service.sponsor.vo.CreateUserRequest;
import com.yoge.ad.service.sponsor.vo.CreateUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/13
 */
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create/user")
    public CreateUserResponse createUser(@RequestBody CreateUserRequest request) throws AdException {
        log.info("ad-sponsor: createUser -> {}", JSON.toJSONString(request));
        return userService.createUser(request);
    }
}
