package com.yoge.ad.service.search.mediaSearch.vo;

import com.yoge.ad.service.search.mediaSearch.vo.feature.DistrictFeature;
import com.yoge.ad.service.search.mediaSearch.vo.feature.FeatureRelation;
import com.yoge.ad.service.search.mediaSearch.vo.feature.ItFeature;
import com.yoge.ad.service.search.mediaSearch.vo.feature.KeywordFeature;
import com.yoge.ad.service.search.mediaSearch.vo.media.AdSlot;
import com.yoge.ad.service.search.mediaSearch.vo.media.Device;
import com.yoge.ad.service.search.mediaSearch.vo.media.Geo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import scala.App;

import java.util.List;

/**
 * @author geyoujia
 * @date 2019/07/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {

    // 媒体方请求标识
    private String mediaId;
    // 请求基本信息
    private RequestInfo requestInfo;
    // 匹配信息
    private FeatureInfo featureInfo;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestInfo {

        /**
         * 请求的唯一 id
         */
        private String requestId;

        /**
         * 广告位, 可以一次请求多个广告位
         */
        private List<AdSlot> adSlots;

        /**
         * app 信息
         */
        private App app;

        /**
         * 地理位置信息
         */
        private Geo geo;

        /**
         * 设备信息
         */
        private Device device;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FeatureInfo {

        private KeywordFeature keywordFeature;
        private ItFeature itFeature;
        private DistrictFeature districtFeature;

        /**
         * 默认为 AND
         */
        private FeatureRelation featureRelation = FeatureRelation.AND;
    }
}
