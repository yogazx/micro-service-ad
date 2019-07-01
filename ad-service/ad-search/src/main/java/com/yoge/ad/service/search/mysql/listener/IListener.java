package com.yoge.ad.service.search.mysql.listener;

import com.yoge.ad.service.search.mysql.dto.BinlogRowData;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/6/5
 */
public interface IListener {

    /**
     * 对不同的表进行不同放入增量数据更新方法，可以通过此方法注册不同的监听器
     */
    void  register();

    /**
     * 监听到事件
     * @param eventData
     */
    void onEvent(BinlogRowData eventData);
}
