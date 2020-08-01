package com.bootbase.domain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: xinput
 * @Date: 2020-06-15 14:12
 */
public class BaseHttp {

    private HttpServletRequest request;

    private HttpServletResponse responsen;

    public BaseHttp() {
    }

    public BaseHttp(HttpServletRequest request, HttpServletResponse responsen) {
        this.request = request;
        this.responsen = responsen;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponsen() {
        return responsen;
    }

    public void setResponsen(HttpServletResponse responsen) {
        this.responsen = responsen;
    }
}


