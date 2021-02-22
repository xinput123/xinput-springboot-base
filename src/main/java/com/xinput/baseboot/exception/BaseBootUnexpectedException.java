package com.xinput.baseboot.exception;

/**
 * @author xinput
 * @date 2020-07-02 14:01
 */
public class BaseBootUnexpectedException extends RuntimeException {

  public BaseBootUnexpectedException(String message) {
    super(message);
  }

  public BaseBootUnexpectedException(Throwable exception) {
    super("Unexpected Error", exception);
  }

  public BaseBootUnexpectedException(String message, Throwable cause) {
    super(message, cause);
  }
}
