package com.yoge.ad.service.common.dump.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdUnitItTable {

    private Long unitId;
    private String itTag;

    public AdUnitItTable(Long unitId, String itTag) {
        this.unitId = unitId;
        this.itTag = itTag;
    }
}
