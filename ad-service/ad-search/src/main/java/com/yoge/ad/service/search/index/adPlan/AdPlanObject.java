package com.yoge.ad.service.search.index.adPlan;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DESC AdPlan 索引计划（推广对象）
 *
 * @author You Jia Ge
 * Created Time 2019/5/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class AdPlanObject {

    private Long planId;
    private Long userId;
    private Integer planStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    AdPlanObject update(AdPlanObject adPlanObject) {
        if (adPlanObject == null)
            return null;
        if (adPlanObject.getPlanId() != null) {
            this.planId = adPlanObject.getPlanId();
        }
        if (adPlanObject.getUserId() != null) {
            this.userId = adPlanObject.getUserId();
        }
        if (adPlanObject.getPlanStatus() != null) {
            this.planStatus = adPlanObject.getPlanStatus();
        }
        if (adPlanObject.getStartDate() != null) {
            this.startDate = adPlanObject.getStartDate();
        }
        if (adPlanObject.getEndDate() != null) {
            this.endDate = adPlanObject.getEndDate();
        }
        return this;
    }
}
