package com.xinput.bootbase.domain;

import com.xinput.bootbase.consts.ErrorCode;

/**
 * @Author: xinput
 * @Date: 2020-06-06 14:37
 */
public class Result {

    private Integer code;

    private Object msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getMessage() {
        return msg;
    }

    public void setMessage(Object msg) {
        this.msg = msg;
    }

    public void setCodeWithDefaultMsg(int code) {
        this.code = code;
        this.msg = ErrorCode.getMsg(code);
    }

    public void setCodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
