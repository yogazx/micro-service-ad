package com.yoge.ad.service.common.dump.table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdCreativeUnitTable {

    private Long id;
    private Long unitId;

    public AdCreativeUnitTable(Long id, Long unitId) {
        this.id = id;
        this.unitId = unitId;
    }
}
