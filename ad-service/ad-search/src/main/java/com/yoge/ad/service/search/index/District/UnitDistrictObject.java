package com.yoge.ad.service.search.index.District;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UnitDistrictObject {

    private Long unitId;
    private String province;
    private String city;
}
