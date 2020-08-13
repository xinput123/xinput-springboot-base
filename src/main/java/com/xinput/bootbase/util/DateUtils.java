package com.xinput.bootbase.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

/**
 * @Author: xinput
 * Date工具类 工具类
 */
public class DateUtils extends DateFormatUtils {

    public static final String DATE_TIME_FORMATTER_STRING = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMATTER_STRING = "yyyy-MM-dd";

    public static final String TIME_FORMATTER_STRING = "HH:mm:ss";

    /**
     * LocalDate -> Date
     */
    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDateTime -> Date
     */
    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static int differentDays(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new RuntimeException("日期不能为空");
        }
        LocalDate localDate1 = LocalDateUtils.asLocalDate(date1);
        LocalDate localDate2 = LocalDateUtils.asLocalDate(date2);

        /**
         * Period 用于计算时间间隔
         */
        return Period.between(localDate1, localDate2).getDays();
    }

}
