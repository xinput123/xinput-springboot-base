package com.xinput.bootbase.exception;

/**
 * @Author: xinput
 * @Date: 2020-07-02 14:01
 */
public class BaseUnexpectedException extends RuntimeException {

    public BaseUnexpectedException(String message) {
        super(message);
    }

    public BaseUnexpectedException(Throwable exception) {
        super("Unexpected Error", exception);
    }

    public BaseUnexpectedException(String message, Throwable cause) {
        super(message, cause);
    }
}
