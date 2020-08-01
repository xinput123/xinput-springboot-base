package com.bootbase.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author: xinput
 * 属性文件工具类
 */
public class PropsUtils {
    private static Logger logger = LoggerFactory.getLogger(PropsUtils.class);

    /**
     * 加载属性文件
     */
    public static Properties loadProps(String fileName) {
        Properties properties = null;
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)) {
            if (null == is) {
                throw new FileNotFoundException(fileName + " file is not found");
            }
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            logger.error("load properties file failure", e);
        }

        return properties;
    }

    /**
     * 获取字符型属性(默认为空字符串)
     */
    public static String getString(Properties properties, String key) {
        return getString(properties, key, "");
    }

    /**
     * 获取字符型属性(可指定默认值)
     */
    public static String getString(Properties properties, String key, String defaultValue) {
        String value = defaultValue;
        if (properties.contains(key)) {
            value = properties.getProperty(key);
        }
        return value;
    }

    /**
     * 获取数值型属性(默认为0)
     */
    public static int getInt(Properties properties, String key) {
        return getInt(properties, key, 0);
    }

    /**
     * 获取数值型属性(可指定默认值)
     */
    public static int getInt(Properties properties, String key, int defaultValue) {
        int value = defaultValue;
        if (properties.contains(key)) {
            value = CastUtils.castInt(properties.getProperty(key));
        }
        return value;
    }

    /**
     * 获取布尔型属性(默认为false)
     */
    public static boolean getBoolean(Properties properties, String key) {
        return getBoolean(properties, key, false);
    }

    /**
     * 获取布尔型属性(可指定默认值)
     */
    public static boolean getBoolean(Properties properties, String key, boolean defaultValue) {
        boolean value = defaultValue;
        if (properties.contains(key)) {
            value = CastUtils.castBoolean(properties.getProperty(key));
        }
        return value;
    }

    /**
     * 获取Long型属性(默认为0L)
     */
    public static long getLong(Properties properties, String key) {
        return getLong(properties, key, 0L);
    }

    /**
     * 获取Long型属性(可指定默认值)
     */
    public static long getLong(Properties properties, String key, long defaultValue) {
        long value = defaultValue;
        if (properties.contains(key)) {
            value = CastUtils.castLong(properties.getProperty(key));
        }
        return value;
    }
}
