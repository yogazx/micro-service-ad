package com.yoge.ad.service.dumps.controller;

import com.yoge.ad.service.common.dump.DConstant;
import com.yoge.ad.service.dumps.service.DumpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author geyoujia
 * @date 2019/07/07
 */
@RestController(value = "/dump")
public class AdDumpController {

    @Autowired
    private DumpService dumpService;

    @GetMapping("/adplan")
    public void dumpAdPlanTable() {
        dumpService.dumpAdPlanTable(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_PLAN));
    }

    @GetMapping("/adunit")
    public void dumpAdUnitTable() {
        dumpService.dumpAdUnitTable(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT));
    }

    @GetMapping("/adunitdistrict")
    public void dumpAdUnitDistrictTable() {
        dumpService.dumpAdUnitDistrictTable(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_DISTRICT));
    }

    @GetMapping("/adunitit")
    public void dumpAdUnitItTable() {
        dumpService.dumpAdUnitItTable(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_IT));
    }

    @GetMapping("/adunitkeyword")
    public void dumpAdUnitKeywordTable() {
        dumpService.dumpAdUnitKeywordTable(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_KEYWORD));
    }

    @GetMapping("/creative")
    public void dumpCreativeTable() {
        dumpService.dumpCreativeTable(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_CREATIVE));
    }

    @GetMapping("/creativeunit")
    public void dumpCreativeUnitTable() {
        dumpService.dumpCreativeUnitTable(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_CREATIVE_UNIT));
    }
}
