package com.bootbase.exception;

import org.springframework.http.HttpStatus;

/**
 * @Author: xinput
 * @Date: 2020-06-06 22:50
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -6005972502557129121L;

    private HttpStatus httpStatus;

    private Integer code;

    private Object msg;

    public BaseException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public BaseException(HttpStatus httpStatus, Integer code) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.msg = super.getMessage();
    }

    public BaseException(HttpStatus httpStatus, Object message) {
        this.httpStatus = httpStatus;
        this.msg = message;
    }

    public BaseException(HttpStatus httpStatus, Integer code, Object message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.msg = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Integer getCode() {
        return code;
    }

    public Object getMsg() {
        return msg;
    }
}
