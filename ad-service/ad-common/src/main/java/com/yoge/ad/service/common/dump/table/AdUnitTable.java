package com.yoge.ad.service.common.dump.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdUnitTable {

    private Long id;
    private Integer unitStatus;
    private Integer positionType;
    private Long planId;

    public AdUnitTable(Long id, Integer unitStatus, Integer positionType, Long planId) {
        this.id = id;
        this.unitStatus = unitStatus;
        this.positionType = positionType;
        this.planId = planId;
    }
}
