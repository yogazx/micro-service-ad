package com.yoge.ad.service.search.mediaSearch;

import com.yoge.ad.service.search.mediaSearch.vo.SearchRequest;
import com.yoge.ad.service.search.mediaSearch.vo.SearchResponse;

/**
 * 媒体方 广告检索请求
 *
 * @author geyoujia
 * @date 2019/07/19
 */
public interface ISearch {

    SearchResponse fetchAds(SearchRequest request);
}
