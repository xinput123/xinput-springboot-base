package com.xinput.bootbase.consts;

/**
 * System.properties中的属性
 *
 * @author xinput
 * @date 2020-06-10 21:48
 */
public class DefaultConsts {

    /**
     * 默认配置文件名称
     */
    public static final String DEFAULT_SYSTEM_FILE = "system.properties";

    /**
     * 设置Cookie中token对应的key值，默认是jwt
     */
    public static final String API_COOKIE_TOKEN = "api.cookie.token";

    /**
     * 是否开启Cookie验证，默认关闭
     */
    public static final String API_COOKIE_ENABLE = "api.cookie.enable";

    /**
     * secure的key
     */
    public static final String API_SECRET_KEY = "api.secret.key";

    /**
     * 是否默认开启全局的token验证，如果某个方法不想验证，可以使用 @PassSecure 注解
     * 在开发环境下，如果有token，验证，否则默认关闭
     * 在测试环境和正式环境下，默认开启
     */
    public static final String API_SECURE_ENABLE = "api.secure.enable";

    /**
     * 在开发环境下默认使用的用户Id
     */
    public static final String MOCK_USER_ID = "mock.userId";

    /**
     * 在开发环境下默认使用的用户名称
     */
    public static final String MOCK_USER_NAME = "mock.userName";

    /**
     * token过期时间
     */
    public static final String API_TOKEN_EXPIRE = "token.exp";

    /**
     * Token刷新时间
     */
    public static final String API__REFRESH_TOKEN_EXPIRE = "refresh.token.exp";

    /**
     * 对象存储的key
     */
    public static final String BUCKET_ACCESS_KEY = "bucket.access.key";

    /**
     * 对象存储的秘钥
     */
    public static final String BUCKET_SECRET_KEY = "bucket.access.secret";

    /**
     * 微信小程序设置:wechat.appid
     */
    public static final String WECHAT_APPID = "wechat.appid";

    /**
     * 微信小程序设置:wechat.secret
     */
    public static final String WECHAT_SECRET = "wechat.secret";

    /**
     * 默认取多少条数据
     */
    public static final String DEFAULT_LIMIT = "limit.default";

    /**
     * 自定义最多取多少条数据
     */
    public static final String MAX_LIMIT = "limit.max";

    /**
     * 自定义最大偏移量
     */
    public static final String MAX_OFFSET = "offset.max";
}
