package com.yoge.ad.service.dumps.service;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/22
 */
public interface DumpService {

    void dumpAdPlanTable(String fileName);

    void dumpAdUnitTable(String fileName);

    void dumpCreativeTable(String fileName);

    void dumpCreativeUnitTable(String fileName);

    void dumpAdUnitDistrictTable(String fileName);

    void dumpAdUnitItTable(String fileName);

    void dumpAdUnitKeywordTable(String fileName);
}
