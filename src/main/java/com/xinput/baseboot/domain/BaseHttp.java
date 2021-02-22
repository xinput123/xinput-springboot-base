package com.xinput.baseboot.domain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xinput
 * @date 2020-06-15 14:12
 */
public class BaseHttp {

  private HttpServletRequest request;

  private HttpServletResponse response;

  public BaseHttp() {
  }

  public BaseHttp(HttpServletRequest request, HttpServletResponse response) {
    this.request = request;
    this.response = response;
  }

  public HttpServletRequest getRequest() {
    return request;
  }

  public void setRequest(HttpServletRequest request) {
    this.request = request;
  }

  public HttpServletResponse getResponse() {
    return response;
  }

  public void setResponse(HttpServletResponse response) {
    this.response = response;
  }
}


