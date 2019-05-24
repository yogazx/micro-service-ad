package com.yoge.ad.service.common.dump.table;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AdPlanTable {

    private Long id;
    private Long userId;
    private Integer planStatus;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public AdPlanTable(Long id, Long userId, Integer planStatus, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.userId = userId;
        this.planStatus = planStatus;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
