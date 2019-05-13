package com.yoge.ad.service.sponsor.controller;

import com.alibaba.fastjson.JSON;
import com.yoge.ad.service.common.exception.AdException;
import com.yoge.ad.service.sponsor.service.CreativeService;
import com.yoge.ad.service.sponsor.vo.CreativeRequest;
import com.yoge.ad.service.sponsor.vo.CreativeResponse;
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
public class CreativeController {

    @Autowired
    private CreativeService creativeService;

    @PostMapping("/create/creative")
    public CreativeResponse createCreative(@RequestBody CreativeRequest request) throws AdException {
        log.info("ad-sponsor: createCreative -> {}", JSON.toJSONString(request));
        return creativeService.createCreative(request);
    }
}
