package com.xinput.bootbase.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Author: xinput
 * LocalDateTime 工具类
 */
public class LocalDateTimeUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * String -> LocalDateTime
     */
    public static LocalDateTime asLocalDate(String dateString) {
        try {
            return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Date -> LocalDateTime
     */
    public static LocalDateTime asLocalDateTime(Date date) {
        try {
            return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * LocalDateTime -> yyyy-MM-dd HH:mm:ss
     */
    public static String dateTimeFormat(LocalDateTime localDateTime) {
        return localDateTime.format(DATE_TIME_FORMATTER);
    }

    /**
     * LocalDateTime -> yyyy-MM-dd
     */
    public static String dateFormat(LocalDateTime localDateTime) {
        return localDateTime.format(DATE_FORMATTER);
    }

    /**
     * LocalDateTime -> HH:mm:ss
     */
    public static String timeFormat(LocalDateTime localDateTime) {
        return localDateTime.format(TIME_FORMATTER);
    }

    /**
     * LocalDateTime -> 自定义格式
     */
    public static String format(LocalDateTime localDateTime, DateTimeFormatter dateTimeFormatter) {
        return localDateTime.format(dateTimeFormatter);
    }

}
