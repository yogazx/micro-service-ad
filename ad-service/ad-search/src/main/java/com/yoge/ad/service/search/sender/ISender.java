package com.yoge.ad.service.search.sender;

import com.yoge.ad.service.search.mysql.dto.MysqlRowData;

/**
 *
 * @author geyoujia
 * @date 2019/07/07
 */
public interface ISender {

    void send(MysqlRowData mysqlRowData);
}
