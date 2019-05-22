package com.yoge.ad.service.common.dump.table;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AdUnitKeywordTable {

    private Long unitId;
    private String keyword;

    public AdUnitKeywordTable(Long unitId, String keyword) {
        this.unitId = unitId;
        this.keyword = keyword;
    }
}
