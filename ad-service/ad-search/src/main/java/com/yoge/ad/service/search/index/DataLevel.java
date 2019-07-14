package com.yoge.ad.service.search.index;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum DataLevel {
    /**
     * 索引层级枚举表示
     * 此处中的 level对应到 resources/template.json中定义的level
     */
    LEVEL_TWO(2, "level_two"),
    LEVEL_THREE(3, "level_three"),
    LEVEL_FOUR(4, "level_four");


    private Integer level;
    private String desc;
}
