package com.xinput.baseboot.config;

import com.google.common.collect.Lists;
import com.xinput.baseboot.consts.BaseBootConsts;
import com.xinput.bleach.util.SimpleProperties;
import com.xinput.bleach.util.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

/**
 * @author xinput
 * @date 2020-06-21 09:46
 */
public class BaseBootConfig {

  private static final long TOKEN_EXP = 3600 * 24;

  private static final long REFRESH_TOKEN_EXP = 3600 * 24 * 7;

  private static SimpleProperties SP;

  static {
    try {
      SP = SimpleProperties.readConfiguration(BaseBootConsts.DEFAULT_SYSTEM_FILE);
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
    return get(BaseBootConsts.MOCK_USER_ID, "1");
  }

  /**
   * 获取配置的用户名称
   *
   * @return
   */
  public static final String getMockUserName() {
    return get(BaseBootConsts.MOCK_USER_NAME, "xinput-001");
  }

  /**
   * cookie中token的值
   */
  public static final String getCookieTokenName() {
    return get(BaseBootConsts.API_COOKIE_TOKEN, "jwt");
  }

  /**
   * cookie验证
   */
  public static final boolean getCookieSecure() {
    return getBoolean(BaseBootConsts.API_COOKIE_ENABLE, Boolean.FALSE);
  }

  /**
   * 是否开启token验证
   */
  public static final boolean getSecureEnable() {
    return getBoolean(BaseBootConsts.API_SECURE_ENABLE, Boolean.TRUE);
  }

  /**
   * token过期时间设置
   */
  public static String getTokenExp() {
    return get(BaseBootConsts.API_TOKEN_EXPIRE, String.valueOf(TOKEN_EXP));
  }

  /**
   * token刷新时间
   */
  public static String getRefreshToeknExp() {
    return get(BaseBootConsts.API__REFRESH_TOKEN_EXPIRE, String.valueOf(REFRESH_TOKEN_EXP));
  }

  public static String getApiSecureKey() {
    return get(BaseBootConsts.API_SECRET_KEY, "xinput-boot");
  }

  /**
   * 获取对象存储的AK
   *
   * @return
   */
  public static String getBucketKey() {
    return get(BaseBootConsts.BUCKET_ACCESS_KEY);
  }

  /**
   * 获取对象存储的SK
   *
   * @return
   */
  public static String getBucketSecretKey() {
    return get(BaseBootConsts.BUCKET_SECRET_KEY);
  }

  /**
   * 默认取多少条数据
   */
  public static int getDefaultLimit() {
    return getInt(BaseBootConsts.DEFAULT_LIMIT, 10);
  }

  /**
   * 默认取多少条数据
   */
  public static int getMaxLimit() {
    return getInt(BaseBootConsts.MAX_LIMIT, 50);
  }

  /**
   * 默认一次最多取多少条数据
   */
  public static int getMaxOffset() {
    return getInt(BaseBootConsts.MAX_OFFSET, 1000000);
  }

  public static final int getInt(String key, int defaultValue) {
    if (SP == null) {
      return 0;
    }

    return SP.getIntProperty(key, defaultValue);
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
