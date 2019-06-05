package com.yoge.ad.service.search.mysql.listener;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.Event;
import com.google.common.collect.Maps;
import com.yoge.ad.service.search.mysql.TemplateHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/28
 */
@Slf4j
@Component
public class AggregationListener implements BinaryLogClient.EventListener {

    private String databaseName;
    private String tableName;

    private Map<String, IListener> listenerMap = Maps.newConcurrentMap();

    private final TemplateHolder templateHolder;

    @Autowired
    public AggregationListener(TemplateHolder templateHolder)  {
        this.templateHolder = templateHolder;
    }

    @Override
    public void onEvent(Event event) {

    }
}
