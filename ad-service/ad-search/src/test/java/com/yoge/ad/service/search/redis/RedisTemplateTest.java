package com.yoge.ad.service.search.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

/**
 * @author geyoujia
 * @date 2019/07/20
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTemplateTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testScanCommand() {
        RedisConnection connection = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection();
        Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match("*").count(Integer.MAX_VALUE).build());
        while (cursor.hasNext()) {
            String key  = new String(cursor.next());
            System.out.println("key : " + key);
//            Object value = redisTemplate.opsForValue().get(key);
//            System.out.println("value : " + value);
        }
    }
}
