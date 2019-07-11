package com.yoge.ad.service.search.index;

import com.alibaba.fastjson.JSON;
import com.yoge.ad.service.common.dump.DConstant;
import com.yoge.ad.service.common.dump.table.*;
import com.yoge.ad.service.search.handler.AdLevelDataHandler;
import com.yoge.ad.service.search.mysql.constant.OperateType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DESC 用于实现全量索引的加载，读取ad-dump数据表导出的文件，加载索引
 *
 * @author You Jia Ge
 * Created Time 2019/5/23
 */
@Slf4j
@Component
@DependsOn(value = {"dataTable"}) // 此处不必要, 因为DataTable注册优先级已经设置为最高, 但为表示出依赖关系顾写此注解
public class IndexFileLoader {

    @PostConstruct
    public void init() {
        // 第二层级 推广计划索引加载
        List<String> adPlanList = loadDump(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_PLAN));
        if (adPlanList.size() != 0 && adPlanList != null) {
            adPlanList.forEach(adPlan -> AdLevelDataHandler.handleLevel2WithPlan(
                    JSON.parseObject(adPlan, AdPlanTable.class),
                    OperateType.ADD));
        }

        // 第二层级 创意索引加载
        List<String> adCreativeList = loadDump(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_CREATIVE));
        if (adCreativeList.size() != 0 && adCreativeList != null) {
            adCreativeList.forEach(adCreative -> AdLevelDataHandler.handleLevel2WithCreative(
                    JSON.parseObject(adCreative, AdCreativeTable.class),
                    OperateType.ADD));
        }

        // 第三层级 推广单元索引加载
        List<String> adUnitList = loadDump(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT));
        if (adUnitList.size() != 0 && adUnitList != null) {
            adUnitList.forEach(adUnit -> AdLevelDataHandler.handleLevel3WithUnit(
                    JSON.parseObject(adUnit, AdUnitTable.class),
                    OperateType.ADD));
        }

        // 第三层级 创意与推广单元关联索引加载
        List<String> adCreativeUnitList = loadDump(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_CREATIVE_UNIT));
        if (adCreativeUnitList.size() != 0 && adCreativeUnitList != null) {
            adCreativeUnitList.forEach(adCreativeUnit -> AdLevelDataHandler.handleLevel3WithCreativeUnit(
                    JSON.parseObject(adCreativeUnit, AdCreativeUnitTable.class),
                    OperateType.ADD));
        }

        // 第四层级  推广单元地域限制 索引加载
        List<String> adUnitDistrictList = loadDump(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_DISTRICT));
        if (adUnitDistrictList.size() != 0 && adUnitDistrictList != null) {
            adUnitDistrictList.forEach(adUnitDistrict -> AdLevelDataHandler.handleLevel4WithUnitDistrict(
                    JSON.parseObject(adUnitDistrict, AdUnitDistrictTable.class),
                    OperateType.ADD
            ));
        }

        // 第四层级 推广单元兴趣限制 索引加载
        List<String> adUnitItList = loadDump(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_IT));
        if (adUnitList.size() != 0 && adUnitList != null) {
            adUnitItList.forEach(adUnitIt -> AdLevelDataHandler.handleLevel4WithUnitIt(
                    JSON.parseObject(adUnitIt, AdUnitItTable.class),
                    OperateType.ADD
            ));
        }

        // 第四层级 推广单元关键词限制 索引加载
        List<String> adUnitKeywordList = loadDump(String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_KEYWORD));
        if (adUnitKeywordList.size() != 0 && adUnitKeywordList != null) {
            adUnitKeywordList.forEach(adUnitKeyword -> AdLevelDataHandler.handleLevel4WithUnitKeyword(
                    JSON.parseObject(adUnitKeyword, AdUnitKeywordTable.class),
                    OperateType.ADD
            ));
        }
    }

    private List<String> loadDump(String fileName) {
        Path path = Paths.get(fileName);
        boolean exists = Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS});
        if (exists) {
            try {
                BufferedReader reader = Files.newBufferedReader(path);
                return reader.lines().collect(Collectors.toList());
            } catch (IOException e) {
                log.error("ad-search IndexFileLoader io exception");
                throw new RuntimeException(e.getMessage());
            }
        }
        return Collections.emptyList();
    }
}
