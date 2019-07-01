package com.yoge.ad.service.search.mysql.dto;

import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author geyoujia
 * @date 2019/06/27
 */
@Data
public class BinlogRowData {

    private TableTemplate tableTemplate;
    private EventType type;

    /**
     * Map中 key: columnName; value: columnValue
     * 两个list分别对应update的前后变化
     */
    private List<Map<String, String>> before;
    private List<Map<String, String>> after;
}
