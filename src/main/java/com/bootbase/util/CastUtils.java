package com.bootbase.util;

/**
 * @Author: xinput
 * 转型操作工具类
 */
public class CastUtils {
    /**
     * 转为字符串
     */
    public static String castString(Object obj) {
        return castString(obj, "");
    }

    /**
     * 转为字符串(提供默认值)
     */
    public static String castString(Object obj, String defaultValue) {
        return obj != null ? String.valueOf(obj) : defaultValue;
    }

    /**
     * 转为double
     */
    public static double castDouble(Object obj) {
        return castDouble(obj, 0);
    }

    /**
     * 转为double(提供默认值)
     */
    public static double castDouble(Object obj, double defaultValue) {
        double doubleValue = defaultValue;
        if (null != obj) {
            String value = castString(obj);
            if (StringUtils.isNotEmpty(value)) {
                try {
                    doubleValue = Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    doubleValue = defaultValue;
                }
            }
        }
        return doubleValue;
    }

    /**
     * 转为long型
     */
    public static long castLong(Object obj) {
        return castLong(obj, 0);
    }

    /**
     * 转为字符串(提供默认值)
     */
    public static long castLong(Object obj, long defaultValue) {
        long longValue = defaultValue;
        if (null != obj) {
            String value = castString(obj);
            if (StringUtils.isNotEmpty(value)) {
                try {
                    longValue = Long.parseLong(value);
                } catch (NumberFormatException e) {
                    longValue = defaultValue;
                }
            }
        }

        return longValue;
    }

    /**
     * 转为int型
     */
    public static int castInt(Object obj) {
        return castInt(obj, 0);
    }

    /**
     * 转为int型(提供默认值)
     */
    public static int castInt(Object obj, int defaultValue) {
        int intValue = defaultValue;
        if (null != obj) {
            String value = castString(obj);
            if (StringUtils.isNotEmpty(value)) {
                try {
                    intValue = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    intValue = defaultValue;
                }
            }
        }

        return intValue;
    }

    /**
     * 转为boolean型
     */
    public static boolean castBoolean(Object obj) {
        return castBoolean(obj, false);
    }

    /**
     * 转为boolean型(提供默认值)
     */
    public static boolean castBoolean(Object obj, boolean defaultValue) {
        boolean booleanValue = defaultValue;
        if (null != obj) {
            String value = castString(obj);
            booleanValue = Boolean.parseBoolean(value);
        }

        return booleanValue;
    }
}
