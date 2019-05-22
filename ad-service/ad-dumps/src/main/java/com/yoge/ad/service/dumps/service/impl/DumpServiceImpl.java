package com.yoge.ad.service.dumps.service.impl;

import com.alibaba.fastjson.JSON;
import com.yoge.ad.service.common.dump.table.*;
import com.yoge.ad.service.dumps.constant.CommonStatus;
import com.yoge.ad.service.dumps.repo.AdCreativeRepository;
import com.yoge.ad.service.dumps.repo.AdPlanRepository;
import com.yoge.ad.service.dumps.repo.AdUnitRepository;
import com.yoge.ad.service.dumps.repo.unitcondition.AdUnitDistrictRepository;
import com.yoge.ad.service.dumps.repo.unitcondition.AdUnitItRepository;
import com.yoge.ad.service.dumps.repo.unitcondition.AdUnitKeywordRepository;
import com.yoge.ad.service.dumps.repo.unitcondition.CreativeUnitRepository;
import com.yoge.ad.service.dumps.service.DumpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/22
 */
@Slf4j
@Service
public class DumpServiceImpl implements DumpService {

    @Autowired
    private AdPlanRepository adPlanRepository;
    @Autowired
    private AdUnitRepository adUnitRepository;
    @Autowired
    private AdCreativeRepository adCreativeRepository;
    @Autowired
    private CreativeUnitRepository creativeUnitRepository;
    @Autowired
    private AdUnitDistrictRepository adUnitDistrictRepository;
    @Autowired
    private AdUnitItRepository adUnitItRepository;
    @Autowired
    private AdUnitKeywordRepository adUnitKeywordRepository;


    /**
     * 导出推广计划
     * @param fileName
     */
    @Override
    public void dumpAdPlanTable(String fileName) {
        List<AdPlanTable> adPlanTableList = adPlanRepository.findByPlanStatus(CommonStatus.VALID.getStatus());
        if (CollectionUtils.isEmpty(adPlanTableList))
            return;
        Path path = Paths.get(fileName);
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            for (AdPlanTable adPlanTable : adPlanTableList) {
                writer.write(JSON.toJSONString(adPlanTable));
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            log.error("dump adPlanTable error");
        }
    }

    /**
     * 导出推广单元
     * @param fileName
     */
    @Override
    public void dumpAdUnitTable(String fileName) {
        List<AdUnitTable> adUnitTableList = adUnitRepository.findByUnitStatus(CommonStatus.VALID.getStatus());
        if (CollectionUtils.isEmpty(adUnitTableList))
            return;
        Path path = Paths.get(fileName);
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            for (AdUnitTable adUnitTable : adUnitTableList) {
                writer.write(JSON.toJSONString(adUnitTable));
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            log.error("dump adUnitTable error");
        }
    }

    /**
     * 创意数据导出
     * @param fileName
     */
    @Override
    public void dumpCreativeTable(String fileName) {
        List<AdCreativeTable> adCreativeTableList = adCreativeRepository.find();
        if (CollectionUtils.isEmpty(adCreativeTableList))
            return;
        Path path = Paths.get(fileName);
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            for (AdCreativeTable adCreativeTable : adCreativeTableList) {
                writer.write(JSON.toJSONString(adCreativeTable));
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            log.error("dump adCreativeTable error");
        }
    }

    /**
     * 导出创意与推广单元关联
     * @param fileName
     */
    @Override
    public void dumpCreativeUnitTable(String fileName) {
        List<AdCreativeUnitTable> adCreativeUnitTableList = creativeUnitRepository.find();
        if (CollectionUtils.isEmpty(adCreativeUnitTableList))
            return;
        Path path = Paths.get(fileName);
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            for (AdCreativeUnitTable adCreativeUnitTable : adCreativeUnitTableList) {
                writer.write(JSON.toJSONString(adCreativeUnitTable));
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            log.error("dump adCreativeUnitTable error");
        }
    }

    /**
     * 导出地域与推广单元关联
     * @param fileName
     */
    @Override
    public void dumpAdUnitDistrictTable(String fileName) {
        List<AdUnitDistrictTable> adUnitDistrictTableList = adUnitDistrictRepository.find();
        if (CollectionUtils.isEmpty(adUnitDistrictTableList))
            return;
        Path path = Paths.get(fileName);
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            for (AdUnitDistrictTable adUnitDistrictTable : adUnitDistrictTableList) {
                writer.write(JSON.toJSONString(adUnitDistrictTable));
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            log.error("dump adUnitDistrictTable error");
        }
    }

    /**
     * 导出兴趣与推广单元关联
     * @param fileName
     */
    @Override
    public void dumpAdUnitItTable(String fileName) {
        List<AdUnitItTable> adUnitItTableList = adUnitItRepository.find();
        if (CollectionUtils.isEmpty(adUnitItTableList))
            return;
        Path path = Paths.get(fileName);
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            for (AdUnitItTable adUnitItTable : adUnitItTableList) {
                writer.write(JSON.toJSONString(adUnitItTable));
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            log.error("dump adUnitItTable error");
        }
    }

    /**
     * 导出关键词与推广单元关联
     * @param fileName
     */
    @Override
    public void dumpAdUnitKeywordTable(String fileName) {
        List<AdUnitKeywordTable> adUnitKeywordTableList = adUnitKeywordRepository.find();
        if (CollectionUtils.isEmpty(adUnitKeywordTableList))
            return;
        Path path = Paths.get(fileName);
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            for (AdUnitKeywordTable adUnitKeywordTable : adUnitKeywordTableList) {
                writer.write(JSON.toJSONString(adUnitKeywordTable));
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            log.error("dump adAdUnitKeywordTable error");
        }
    }

}
