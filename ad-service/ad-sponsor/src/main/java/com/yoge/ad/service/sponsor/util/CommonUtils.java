package com.yoge.ad.service.sponsor.util;

import com.yoge.ad.service.common.exception.AdException;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * DESC
 *
 * @author You Jia Ge
 * Created Time 2019/5/12
 */
public class CommonUtils {

    // 这里我们假设只满足这一种格式的时间字符串
//    private static final String[] LOCAL_DATE_TIME_PARSE_PATTERN = {"yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss", "yyyy.MM.dd HH:mm:ss"};
    private static final String LOCAL_DATE_TIME_PARSE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static DateTimeFormatter formatter;

    public static String md5(String originString) {
        return DigestUtils.md5Hex(originString).toUpperCase();
    }

    /**
     * 将字符串表示的时间转换为LocalDateTime类型
     * @param localDateTimeString
     * @return
     */
    public static LocalDateTime parseString2LocalDateTime(String localDateTimeString) throws AdException {
//        for (String s : LOCAL_DATE_TIME_PARSE_PATTERN) {
//            formatter = DateTimeFormatter.ofPattern(s, Locale.getDefault());
//        formatter = DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PARSE_PATTERN, Locale.getDefault());
        formatter = DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PARSE_PATTERN);
        try {
                return LocalDateTime.parse(LOCAL_DATE_TIME_PARSE_PATTERN, formatter);
            } catch (DateTimeParseException ignored) {}
//        }
        throw new AdException("unable parse String to LocalDateTime");
    }
}
