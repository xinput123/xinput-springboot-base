package com.xinput.bootbase.consts;

import com.xinput.bootbase.annotation.Remark;

/**
 * @Author: xinput
 * @Date: 2020-06-09 22:50
 */
public class HeaderConsts {

    @Remark("请求id")
    public final static String REQUEST_ID_KEY = "X-Request-Id";

    @Remark("Session Id")
    public final static String SESSION_ID_KEY = "X-Session-Id";

    @Remark("请求开始时间")
    public final static String START_TIME_KEY = "StartTime";

    @Remark("返回总数，用于前端计算分页")
    public final static String TOTOL_COUNT_KEY = "X-Total-Count";

    @Remark("默认返回类型")
    public final static String DEFAULT_CONTENT_TYPE_KEY = "application/json; charset=utf-8";

    @Remark("接受的header参数")
    public final static String ACCESS_CONTROL_EXPOSE_HEADERS_KEY = "Access-Control-Expose-Headers";

    @Remark("默认接受的header的值")
    public final static String ACCESS_CONTROL_EXPOSE_HEADERS_VALUE = "Origin, Authorization, Content-Type, If-Match, If-Modified-Since, If-None-Match, If-Unmodified-Since, Accept-Encoding, X-Request-Id";

    @Remark("http请求方法")
    public final static String ACCESS_CONTROL_ALLOW_METHODS_KEY = "Access-Control-Allow-Methods";

    @Remark("http请求方法")
    public final static String ACCESS_CONTROL_ALLOW_METHODS_VALUE = "OPTIONS, GET, POST, PATCH, PUT, DELETE";

    public final static String ACCESS_CONTROL_MAX_AGE_KEY = "Access-Control-Max-Age";

    public final static String ACCESS_CONTROL_MAX_AGE_VALUE = "86400";

    @Remark("设置文件名")
    public final static String CONTENT_DISPOSITION_KEY = "Content-Disposition";
}
