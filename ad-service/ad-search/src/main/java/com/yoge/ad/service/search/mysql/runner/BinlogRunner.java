package com.yoge.ad.service.search.mysql.runner;

import com.yoge.ad.service.search.mysql.BinlogClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/28
 */
@Slf4j
@Component
public class BinlogRunner implements CommandLineRunner {

    @Autowired
    private BinlogClient client;

    @Override
    public void run(String... args) throws Exception {
        log.info("coming in binlog runner");
        client.connect();
    }
}
