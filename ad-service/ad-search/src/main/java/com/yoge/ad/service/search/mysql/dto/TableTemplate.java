package com.yoge.ad.service.search.mysql.dto;

import com.yoge.ad.service.search.mysql.constant.OperateType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DESC 在操作时读取具体信息的实体类
 *
 * @author You Jia Ge
 * Created Time 2019/5/27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableTemplate {

    private String tableName;
    /**
     * 索引加载层级
     */
    private Integer level;
    /**
     * 操作类型 ---> 受此操作影响的数据库列
     */
    private Map<OperateType, List<String>> operateTypeFieldMap = new HashMap<>();
    /**
     * 字段索引 ---> 字段名映射
     * 因为监听到的日志中不能直接获取到字段名而是 0, 1, 2, 3 所以使用该Map将其对应到列名上
     */
    private Map<Integer, String> positionMap = new HashMap<>();
}
