package com.bootbase.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WechatUserInfo {

    /**
     * 小程序用户唯一标识
     */
    @JsonProperty("openid")
    private String openId;

    /**
     * 会话密钥
     */
    @JsonProperty("session_key")
    private String sessionKey;

    /**
     * 用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回
     */
    @JsonProperty("unionid")
    private String unionId;

    /**
     * 错误码: -1--系统繁忙，此时请开发者稍候再试;0--请求成功;40029--code无效;45011--频率限制，每个用户每分钟100次;
     */
    @JsonProperty("errcode")
    private int errCode = 0;

    /**
     * 错误信息
     */
    @JsonProperty("errmsg")
    private String errMsg;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
