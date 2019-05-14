package com.yoge.ad.service.search.client;

import com.yoge.ad.service.common.vo.CommonResponse;
import com.yoge.ad.service.search.client.vo.AdPlan;
import com.yoge.ad.service.search.client.vo.AdPlanGetRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/14
 */
@FeignClient(value = "ad-sponsor-client")
public interface SponsorClient {

    @PostMapping("/ad-sponsor/get/adPlan")
    CommonResponse<List<AdPlan>> getAdPlanListByIdList(@RequestBody AdPlanGetRequest request);

}
