package com.yoge.ad.service.search.mysql;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.yoge.ad.service.search.mysql.config.BinlogConfig;
import com.yoge.ad.service.search.mysql.listener.AggregationListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/25
 */
@Slf4j
@Component
public class BinlogClient {

    @Autowired
    private BinlogConfig binlogConfig;

    @Autowired
    private AggregationListener listener;

    private BinaryLogClient client;

    public void connect() {
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("binlog-thread-%d").build();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1), factory, new ThreadPoolExecutor.AbortPolicy());
        executor.submit(() -> {
            client = new BinaryLogClient(binlogConfig.getHost(), binlogConfig.getPort(), binlogConfig.getUsername(), binlogConfig.getPassword());
            if (StringUtils.isNotBlank(binlogConfig.getBinlogName()) && binlogConfig.getPosition() != -1l) {
                client.setBinlogFilename(binlogConfig.getBinlogName());
                client.setBinlogPosition(binlogConfig.getPosition());
            }
            client.setBlocking(false);
            client.setKeepAlive(false);
            client.registerEventListener(listener);
            try {
                log.info("connecting to mysql...");
                client.connect();
                log.info("connected to mysql");
            } catch (Exception e) {
                log.error("mysql binlog connect error, exception : {}", e);
            }
        });
        executor.shutdown();
//        if (executor.isShutdown()) {
//            close();
//        }
    }

    public void close() {
        try {
            client.disconnect();
        } catch (Exception e) {
            log.error("mysql binlog disconnect error, exception : {}", e);
        }
    }
}
