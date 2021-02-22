package com.xinput.baseboot.consts;

import com.xinput.baseboot.annotation.Remark;

import java.lang.reflect.Field;
import java.util.Hashtable;

/**
 * 返回码定义.
 */
public final class BaseBootError {
  private BaseBootError() {
  }

  /**
   * generic code
   */
  @Remark("未知错误")
  public static final int UNKNOWN = -1;

  @Remark("成功")
  public static final int SUCCESS = 0;

  /*
   * 客户端错误.
   */

  /**
   * 消息格式错误
   */
  @Remark("消息格式错误")
  public static final int CLIENT_FORMAT_ERROR = 1100;

  /**
   * 身份验证失败
   */
  @Remark("身份验证失败")
  public static final int CLIENT_AUTH_ERROR = 1200;

  @Remark("身份令牌过期")
  public static final int CLIENT_AUTH_TOKEN_EXPIRED = 1210;
  /**
   * 操作超时
   */
  @Remark("操作超时")
  public static final int CLIENT_TIMEOUT = 1300;

  /**
   * 访问被拒绝
   */
  @Remark("访问被拒绝")
  public static final int CLIENT_ACCESS_DENIED = 1400;

  /**
   * 客户端超时退出
   */
  @Remark("客户端超时退出")
  public static final int CLIENT_TIMEOUT_LOCKED = 1500;

  /**
   * 找不到资源
   */
  @Remark("找不到资源")
  public static final int CLIENT_RESOURCE_NOT_FOUND = 2100;

  /**
   * 余额不足
   */
  @Remark("余额不足")
  public static final int CLIENT_CREDIT_LOWER_LIMIT = 2400;

  /**
   * 超过配额
   */
  @Remark("超过配额")
  public static final int CLIENT_OVER_QUOTA = 2500;

  /*
   * 服务端错误
   */
  /**
   * 服务器内部错误
   */
  @Remark("内部错误")
  public static final int SERVER_INTERNAL_ERROR = 5000;

  /**
   * 服务器繁忙
   */
  @Remark("服务器繁忙")
  public static final int SERVER_BUSY = 5100;

  /**
   * 资源不足
   */
  @Remark("资源不足")
  public static final int SERVER_RESOURCE_LIMIT = 5200;

  /**
   * 服务更新中
   */
  @Remark("服务更新中")
  public static final int SERVER_UPDATE = 5300;

  static final Hashtable<Integer, String> CODE_MSG_MAP = new Hashtable<>(
      20);

  static {
    Field[] fields = BaseBootError.class.getFields();
    for (Field field : fields) {
      if (field.isAnnotationPresent(Remark.class)) {
        try {
          CODE_MSG_MAP.put(field.getInt(null),
              field.getAnnotation(Remark.class).value());
        } catch (IllegalArgumentException | IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static String getMsg(int responseCode) {
    if (CODE_MSG_MAP.containsKey(responseCode)) {
      return CODE_MSG_MAP.get(responseCode);
    }
    return "未知错误";

  }

}
