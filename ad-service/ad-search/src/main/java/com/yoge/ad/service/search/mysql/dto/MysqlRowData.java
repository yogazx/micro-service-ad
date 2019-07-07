package com.yoge.ad.service.search.mysql.dto;

import com.yoge.ad.service.search.mysql.constant.OperateType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 投递的增量数据实体(目前不支持多数据库)
 * @author geyoujia
 * @date 2019/07/06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MysqlRowData {

    // private String databaseName;目前不支持多数据库
    private String tableName;
    private Integer level;
    private OperateType operateType;
    private List<Map<String, String>> fieldValueMapList;

}
