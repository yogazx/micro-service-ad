package com.yoge.ad.service.search.sender.kafka;

import com.alibaba.fastjson.JSON;
import com.yoge.ad.service.search.mysql.dto.MysqlRowData;
import com.yoge.ad.service.search.sender.ISender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * 将增量数据投递到Kafka
 *
 * @author geyoujia
 * @date 2019/07/07
 */
@Component
public class KafkaSender implements ISender {

    public static final Logger LOGGER = LoggerFactory.getLogger(KafkaSender.class);

    @Value(value = "${adconf.kafka.topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void send(MysqlRowData mysqlRowData) {
        LOGGER.info("开始向Kafka投递增量数据");
        kafkaTemplate.send(topic, JSON.toJSONString(mysqlRowData));
    }
}
