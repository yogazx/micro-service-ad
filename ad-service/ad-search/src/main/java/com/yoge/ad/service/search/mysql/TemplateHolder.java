package com.yoge.ad.service.search.mysql;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.yoge.ad.service.search.mysql.constant.OperateType;
import com.yoge.ad.service.search.mysql.dto.TableTemplate;
import com.yoge.ad.service.search.mysql.dto.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/27
 */
@Slf4j
@Component
public class TemplateHolder {

    private ParseTemplate parseTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String SQL = " SELECT table_schema, table_name, column_name, ordinal_position"
            + " FROM information_schema.columns "
            + " WHERE table_schema = ?"
            + " AND table_name = ?";

    /**
     * bean容器加载完TemplateHolder后就会执行该方法解析template.json
     */
    @PostConstruct
    public void init() {
        loadJson("template.json");
    }

    public TableTemplate getTableTemplate(String tableName) {
        return parseTemplate.getTableTemplateMap().get(tableName);
    }

    private void loadJson(String path) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            InputStream inputStream = classLoader.getResourceAsStream(path);
            Template template = JSON.parseObject(inputStream, StandardCharsets.UTF_8, Template.class, Feature.IgnoreNotMatch);
            this.parseTemplate = ParseTemplate.parse(template);
            loadMeta();
        } catch (IOException e) {
            log.error("load template.json error, {}", e.getMessage());
            throw new RuntimeException("fail to load json file");
        }

    }

    /**
     * 加载数据表元信息
     */
    private void loadMeta() {
        this.parseTemplate.getTableTemplateMap().forEach((key, tableTemplate) -> {
            List<String> updateFields = tableTemplate.getOperateTypeFieldMap().get(OperateType.UPDATE);
            List<String> insertFields = tableTemplate.getOperateTypeFieldMap().get(OperateType.ADD);
            List<String> deleteFields = tableTemplate.getOperateTypeFieldMap().get(OperateType.DELETE);
            jdbcTemplate.query(
                    SQL,
                    new Object[]{parseTemplate.getDatabaseName(), tableTemplate.getTableName()},
                    // 注意：这里返回的是多行结果集，所以不能只用resultSet
                    (resultSet, rowNum) -> {
                        int position = resultSet.getInt("ordinal_position");
                        String columnName = resultSet.getString("column_name");
                        // 判断该字段是否是在自定义的template.json文件中出现，如果出现则代表该字段是有用的
                        if ((updateFields != null && updateFields.contains(columnName)) || (insertFields != null && insertFields.contains(columnName)) || (deleteFields != null && deleteFields.contains(columnName))) {
                            // 查询语句查询出来索引是从 1 开始, binlog-connector-java的索引是从0开始, 所以需要position - 1
                            tableTemplate.getPositionMap().put(position - 1, columnName);
                        }
                        return null;
                    }
            );
        });
    }

}
