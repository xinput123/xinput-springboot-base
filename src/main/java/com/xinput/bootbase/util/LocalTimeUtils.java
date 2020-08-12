package com.xinput.bootbase.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: xinput
 * LocalTime 工具类
 */
public class LocalTimeUtils {

    /**
     * String -> LocalTime
     */
    public static LocalTime asLocalDate(String dateString) {
        return LocalTime.parse(dateString, DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

}
