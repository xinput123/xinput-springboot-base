package com.bootbase.config;

import com.google.common.collect.Lists;
import com.bootbase.consts.DefaultConsts;
import com.bootbase.util.SimpleProperties;
import com.bootbase.util.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

/**
 * @Author: xinput
 * @Date: 2020-06-21 09:46
 */
public class DefaultConfig {

    private static SimpleProperties SP;

    static {
        try {
            SP = SimpleProperties.readConfiguration(DefaultConsts.DEFAULT_SYSTEM_FILE);
        } catch (Exception e) {
            SP = null;
        }
    }

    /**
     * 获取配置的用户id
     *
     * @return
     */
    public static final String getMockUserId() {
        return get(DefaultConsts.MOCK_USER_ID);
    }

    /**
     * 获取配置的用户名称
     *
     * @return
     */
    public static final String getMockUserName() {
        return get(DefaultConsts.MOCK_USER_NAME);
    }

    /**
     * cookie中token的值
     */
    public static final String getCookieTokenName() {
        return get(DefaultConsts.API_COOKIE_TOKEN, "jwt");
    }

    /**
     * cookie验证
     */
    public static final boolean getCookieSecure() {
        return getBoolean(DefaultConsts.API_COOKIE_ENABLE, Boolean.FALSE);
    }

    /**
     * 是否开启token验证
     */
    public static final boolean getSecureEnable() {
        return getBoolean(DefaultConsts.API_COOKIE_ENABLE, Boolean.TRUE);
    }

    /**
     * token过期时间设置
     */
    public static String getTokenExp() {
        return get(DefaultConsts.API_TOKEN_EXPIRE, "3600");
    }

    /**
     * token刷新时间
     */
    public static String getRefreshToeknExp() {
        return get(DefaultConsts.API__REFRESH_TOKEN_EXPIRE, "86400");
    }

    public static String getApiSecureKey() {
        return get(DefaultConsts.API_SECRET_KEY, "xinput");
    }

    /**
     * 获取微信小程序id
     */
    public static String getWechatAppid() {
        return get(DefaultConsts.WECHAT_APPID);
    }

    /**
     * 获取微信小程序 secret
     */
    public static String getWechatSecret() {
        return get(DefaultConsts.WECHAT_SECRET);
    }

    /**
     * 获取对象存储的AK
     *
     * @return
     */
    public static String getBucketKey() {
        return get(DefaultConsts.BUCKET_ACCESS_KEY);
    }

    /**
     * 获取对象存储的SK
     *
     * @return
     */
    public static String getBucketSecretKey() {
        return get(DefaultConsts.BUCKET_SECRET_KEY);
    }

    /**
     * 获取自定义key对应的value
     *
     * @param key
     * @return
     */
    public static final String get(String key) {
        return get(key, StringUtils.EMPTY);
    }

    /**
     * 获取自定义key对应的value,如果不存在，使用默认值 defaultValue
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static final String get(String key, String defaultValue) {
        if (SP == null) {
            return defaultValue;
        }

        return SP.getStringProperty(key, defaultValue);
    }

    public static final boolean getBoolean(String key) {
        return getBoolean(key, Boolean.FALSE);
    }

    public static final boolean getBoolean(String key, boolean defaultValue) {
        if (SP == null) {
            return defaultValue;
        }

        return SP.getBooleanProperty(key, defaultValue);
    }

    public static final List<String> getList(String key) {
        return getList(key, Lists.newArrayList());
    }

    public static final List<String> getList(String key, List<String> defaultList) {
        if (SP == null) {
            return defaultList;
        }

        String[] arrs = SP.getStringArrayProperty(key);
        if (ArrayUtils.isEmpty(arrs)) {
            return Lists.newArrayList(arrs);
        }

        return defaultList;
    }
}
