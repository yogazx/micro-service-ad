package com.yoge.ad.service.search.util;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/19
 */
@Slf4j
public class CommonUtils {

    /**
     * 若map中不存在传入的key，则用传进来的factory构建一个新的value对象
     * @param key
     * @param map
     * @param factory
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> V getOrCreateMap(K key, Map<K, V> map, Supplier<V> factory) {
        return map.computeIfAbsent(key, k -> factory.get());
    }

    public static String stringContact(String... args) {
        StringBuilder builder = new StringBuilder();
        for (String arg : args) {
            builder.append(arg);
            builder.append("-");
        }
        return builder.deleteCharAt(builder.lastIndexOf("-")).toString();
    }

    /**
     * 解析binlog日志可知两点：1.日期在binlog中会转化成类似于Sun Jul 07 22:58:53 CST 2019的字符串
     *                      2.由于时区原因，日期会比在数据库中实际添加的增加8小时
     * @param dateTimeString
     * @return
     */
    public static LocalDateTime stringToLocalDateTime(String dateTimeString) {
        try {
            return LocalDateTime.parse(
                    dateTimeString,
                    DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US))
                    .minusHours(8);
        }catch (DateTimeParseException e) {
            log.error("解析binlog时间字符串出错");
            return null;
        }

    }
}
