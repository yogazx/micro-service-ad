package com.yoge.ad.service.search.util;

import com.google.common.collect.Sets;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

/**
 * @author geyoujia
 * @date 2019/07/19
 */
public class RedisUtil {

    /**
     *
     * @param redisTemplate
     * @param pattern 查询条件
     * @param count count是每次扫描的key个数，并不是结果集个数。count要根据扫描数据量大小而定，Scan虽然无锁，但是也不能保证在超过百万数据量级别搜索效率；
     *              count不能太小，网络交互会变多，count要尽可能的大。在搜索结果集1万以内，建议直接设置为与所搜集大小相同
     * @return
     */
    public static Set<String> scan(RedisTemplate<String, ?> redisTemplate, String pattern, int count) {
        ScanOptions scanOptions;
        if (count > -1) {
            scanOptions = ScanOptions.scanOptions().match(pattern).count(count).build();
            System.out.println("命令：" + scanOptions.toOptionString());
        } else {
            scanOptions = ScanOptions.scanOptions().match(pattern).build();
        }
        // SCAN命令需要在同一条连接上执行
        ConvertingCursor<byte[], String> cursor = redisTemplate.executeWithStickyConnection(connection -> new ConvertingCursor<>(connection.scan(scanOptions), new StringRedisSerializer()::deserialize));
        // 因为使用scan命令可能会存在重复，所以使用HashSet去重
        if (cursor != null) {
//            cursor.getCursorId();
            Set<String> set = Sets.newHashSet();
            cursor.forEachRemaining(set::add);
            return set;
        }
        try {
            cursor.close();
        } catch (IOException e) {

        }
        return Collections.emptySet();
    }
}
