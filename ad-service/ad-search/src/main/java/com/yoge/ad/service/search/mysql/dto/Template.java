package com.yoge.ad.service.search.mysql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DESC 对应template.json的实体类
 *
 * @author You Jia Ge
 * Created Time 2019/5/27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Template {

    private String database;
    private List<JsonTable> tableList;
}
