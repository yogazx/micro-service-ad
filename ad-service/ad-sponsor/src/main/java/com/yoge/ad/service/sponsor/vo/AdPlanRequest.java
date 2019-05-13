package com.yoge.ad.service.sponsor.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdPlanRequest {

    private Long id;
    private Long userId;
    private String planName;
    private String startDate;
    private String endDate;

    public boolean createPlanValidate() {
        return userId != null
                && StringUtils.isNotBlank(planName)
                && StringUtils.isNotBlank(startDate)
                && StringUtils.isNotBlank(endDate);
    }

    public boolean updatePlanValidate() {
        return id != null && userId != null;
    }

    public boolean deletePlanValidate() {
        return id != null && userId != null;
    }
}
