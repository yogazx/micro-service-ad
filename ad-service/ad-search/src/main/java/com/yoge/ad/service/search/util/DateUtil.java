package com.yoge.ad.service.search.util;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Seconds;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


public class DateUtil {

    public static final String LDATE_FORMAT = "yyyyMMdd";
    public static final String TARGET_PATTERN = "yyyy-MM-dd";
    public static final String ADATE_FORMAT = "yyyy年MM月dd日";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_MMdd = "MM月dd日";

    private static final DateTimeFormatter H_M_S_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    public static Date today(){
        return Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date yesterday() {
        return Date.from(LocalDate.now().minusDays(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date endOfToday(){
        return Date.from(LocalDateTime.now().with(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
    }
    
    public static int diffDay(Date source, Date target) {
        DateTime sourceTime = new DateTime(source);
        DateTime targetTime = new DateTime(target);
        return Days.daysBetween(sourceTime, targetTime).getDays();
    }

    public static String format(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
        return sdf.format(date);
    }

    public static String format(Date date,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    
    public static String format(String date, String sourcePattern, String targetPattern){
        if (StringUtils.isBlank(date)){
            return "";
        }
        Date dateFormat = parseDate(date, sourcePattern);
        return format(dateFormat, targetPattern);
    }

    public static Date parseDate(String time, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            return null;
        }
    }

    public static LocalTime parseLocalTime(String times){
        return LocalTime.from(H_M_S_FORMATTER.parse(times));
    }

    public static String format4YYYYMMDDHHMISS(Date date) {
        return format(date, "yyyyMMddHHmmss");
    }
    
    public static Date addDays(Date date, int amount) {
        return add(date, 5, amount);
    }

    public static Date addMonth(Date date, int amount) {
        return add(date, 2, amount);
    }

    public static Date addYear(Date date, int amount) {
        return add(date, Calendar.YEAR, amount);
    }

    public static Date addHours(Date date, int amount) {
        return add(date, 10, amount);
    }

    public static Date addMinutes(Date date, int amount) {
        return add(date, 12, amount);
    }

    public static Date addSeconds(Date date, int amount) {
        return add(date, 13, amount);
    }

    public static Date dayEnd(Date someday) {
        return new Date(dayStart(someday).getTime() + 86400000L - 1L);
    }

    public static Date dayStart(Date someday) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(someday);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTime();
    }

    /**
     * 时间差:精确到秒
     */
    public static int diff(Date source, Date target) {
        DateTime sourceTime = new DateTime(source);
        DateTime targetTime = new DateTime(target);
        return Seconds.secondsBetween(sourceTime, targetTime).getSeconds();
    }
    
    private static Date add(Date date, int zoom, int amount) {
        if(date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else if(amount == 0) {
            return date;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(zoom, amount);
            return cal.getTime();
        }
    }
}
