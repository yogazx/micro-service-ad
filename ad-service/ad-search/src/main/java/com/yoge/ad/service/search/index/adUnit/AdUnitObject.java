package com.yoge.ad.service.search.index.adUnit;

import com.yoge.ad.service.search.index.adPlan.AdPlanObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AdUnitObject {

    private Long unitId;
    private Integer unitStatus;
    private Integer positionType;
    private Long planId;
    private AdPlanObject adPlanObject;

    AdUnitObject update(AdUnitObject object) {
        if (object == null) {
            return this;
        }
        if (object.getUnitId() != null) {
            this.unitId = object.getUnitId();
        }
        if (object.getUnitStatus() != null) {
            this.unitStatus = object.getUnitStatus();
        }
        if (object.getPositionType() != null) {
            this.positionType = object.getPositionType();
        }
        if (object.getPlanId() != null) {
            this.planId = object.getPlanId();
        }
        if (object.getAdPlanObject() != null) {
            this.adPlanObject = object.getAdPlanObject();
        }
        return this;
    }
}
