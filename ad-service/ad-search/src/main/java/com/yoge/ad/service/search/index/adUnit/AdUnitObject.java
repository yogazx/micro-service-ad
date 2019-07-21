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

    private static boolean isKaiPing(int positionType) {
        return (positionType & AdUnitConstants.PositionType.KAI_PING) > 0;
    }

    private static boolean isTiePian(int positionType) {
        return (positionType & AdUnitConstants.PositionType.TIE_PIAN) > 0;
    }

    private static boolean isTiePianMiddle(int positionType) {
        return (positionType & AdUnitConstants.PositionType.TIE_PIAN_MIDDLE) > 0;
    }

    private static boolean isTiePianPause(int positionType) {
        return (positionType & AdUnitConstants.PositionType.TIE_PIAN_PAUSE) > 0;
    }

    private static boolean isTiePianPost(int positionType) {
        return (positionType & AdUnitConstants.PositionType.TIE_PIAN_POST) > 0;
    }

    public static boolean isAdSlotTypeOk(int adSlotType, int positionType) {
        switch (adSlotType) {
            case AdUnitConstants.PositionType.KAI_PING :
                return isKaiPing(positionType);
            case AdUnitConstants.PositionType.TIE_PIAN:
                return isTiePian(positionType);
            case AdUnitConstants.PositionType.TIE_PIAN_MIDDLE:
                return isTiePianMiddle(positionType);
            case AdUnitConstants.PositionType.TIE_PIAN_PAUSE:
                return isTiePianPause(positionType);
            case AdUnitConstants.PositionType.TIE_PIAN_POST:
                return isTiePianPost(positionType);
            default:
                return false;
        }
    }
}
