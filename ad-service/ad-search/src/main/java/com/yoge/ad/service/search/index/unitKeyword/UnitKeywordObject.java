package com.yoge.ad.service.search.index.unitKeyword;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DESC 关键词索引对象
 *
 * @author You Jia Ge
 * Created Time 2019/5/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UnitKeywordObject {

    private String keyword;
    private Long unitId;
}
