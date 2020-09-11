package com.xinput.baseboot.exception;

import org.springframework.http.HttpStatus;

/**
 * @author xinput
 * @date 2020-06-06 22:50
 */
public class BaseBootException extends RuntimeException {

    private static final long serialVersionUID = -6005972502557129121L;

    private HttpStatus httpStatus;

    private Integer code;

    private Object msg;

    public BaseBootException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public BaseBootException(HttpStatus httpStatus, Integer code) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.msg = super.getMessage();
    }

    public BaseBootException(HttpStatus httpStatus, Object message) {
        this.httpStatus = httpStatus;
        this.msg = message;
    }

    public BaseBootException(HttpStatus httpStatus, Integer code, Object message) {
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
