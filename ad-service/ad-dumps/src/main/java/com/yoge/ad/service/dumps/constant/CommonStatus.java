package com.yoge.ad.service.dumps.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum CommonStatus {

    /**
     * 用户有效状态
     */
    VALID(1, "有效状态"),
    /**
     * 用户无效状态
     */
    IN_VALID(0, "无效状态");

    private Integer status;
    private String desc;


}
