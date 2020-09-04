package com.xinput.bootbase.interceptor;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.xinput.bleach.consts.BaseConsts;
import com.xinput.bleach.util.ObjectId;
import com.xinput.bleach.util.StringUtils;
import com.xinput.bootbase.annotation.PassSecure;
import com.xinput.bootbase.config.SpringContentUtils;
import com.xinput.bootbase.consts.ErrorCode;
import com.xinput.bootbase.consts.HeaderConsts;
import com.xinput.bootbase.domain.BaseHttp;
import com.xinput.bootbase.domain.JwtToken;
import com.xinput.bootbase.domain.Result;
import com.xinput.bootbase.exception.BaseException;
import com.xinput.bootbase.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 定义拦截器
 * 对请求添加requestId，方便追踪
 *
 * @author xinput
 * @date 2020-06-09 22:44
 */
@Component
public class BaseHandlerInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private ThreadLocal<BaseHttp> baseHttpThreadLocal;

    /**
     * 请求处理之前
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        HttpMethod.OPTIONS.toString();
        if (HttpMethod.OPTIONS.toString().equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        baseHttpThreadLocal.set(new BaseHttp(request, response));
        setHeader(request, response);
        securtJwt(request, object);
        // 需要返回true，否则请求不会被控制器处理
        return true;
    }

    /**
     * 统一设置header参数
     *
     * @param request  request
     * @param response response
     */
    private void setHeader(HttpServletRequest request, HttpServletResponse response) {
        String requestId = (String) request.getAttribute(HeaderConsts.REQUEST_ID_KEY);
        if (StringUtils.isNotEmpty(requestId)) {
            requestId = new StringBuilder(20).append(ObjectId.get().toString()).append("-").append(requestId).toString();
        } else {
            requestId = ObjectId.get().toString();
        }
        // request id
        request.setAttribute(HeaderConsts.REQUEST_ID_KEY, requestId);

        //session id
        String sessionId = request.getSession().getId();
        if (StringUtils.isNotEmpty(sessionId)) {
            response.addHeader(HeaderConsts.SESSION_ID_KEY, sessionId);
        }

        //set default content type
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        // default content type without utf-8
        if (MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(response.getContentType())) {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        }

        //set cors
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");

        // set Access-Control-Expose-Headers
        response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HeaderConsts.ACCESS_CONTROL_EXPOSE_HEADERS_VALUE);

        if (HttpMethod.OPTIONS.toString().equalsIgnoreCase(request.getMethod())) {
            // Access-Control-Allow-Methods
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, HeaderConsts.ACCESS_CONTROL_ALLOW_METHODS_VALUE);
            response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, HeaderConsts.ACCESS_CONTROL_MAX_AGE_VALUE);
        }
    }

    /**
     * 验证token
     *
     * @param request request
     * @param object  object
     */
    private void securtJwt(HttpServletRequest request, Object object) {
        // 如果不是映射到方法直接放行
        if (!(object instanceof HandlerMethod)) {
            return;
        }

        // 如果是dev环境，直接放行
        if (BaseConsts.MODE_ACTIVE_DEV.equalsIgnoreCase(SpringContentUtils.getActiveProfile())) {
            return;
        }

        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();

        //检查是否有 PassSecure 注释，有则跳过认证
        if (method.isAnnotationPresent(PassSecure.class)) {
            PassSecure passToken = method.getAnnotation(PassSecure.class);
            if (passToken.required()) {
                return;
            }
        }

        // 从 http 请求头中取出 token
        String token = StringUtils.trim(
                StringUtils.substringAfter(
                        request.getHeader("authorization"), "Bearer")
        );
        Result result = new Result();
        result.setCodeWithDefaultMsg(ErrorCode.CLIENT_AUTH_ERROR);

        if (StringUtils.isNullOrEmpty(token)) {
            unauthorized(result);
            return;
        }

        try {
            final JwtToken jwtToken = JwtUtils.verifyJwtToken(token);
            request.setAttribute(JwtUtils.AUD, jwtToken.getAud());
        } catch (TokenExpiredException e) {
            result.setCodeWithDefaultMsg(ErrorCode.CLIENT_AUTH_TOKEN_EXPIRED);
            unauthorized(result);
            return;
        } catch (Exception e) {
            result.setCodeWithDefaultMsg(ErrorCode.CLIENT_AUTH_ERROR);
            unauthorized(result);
        }
    }

    /**
     * Send 401 Unauthorized
     */
    private void unauthorized(Result result) {
        throw new BaseException(HttpStatus.UNAUTHORIZED, result.getCode(), result.getMessage());
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后），如果异常发生，则该方法不会被调用,所以需要全局异常捕获中清理 session
     *
     * @param request      object
     * @param response     response
     * @param handler      handler
     * @param modelAndView modelAndView
     * @throws Exception Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 清空Tomcat创建的大量session对象
        request.getSession().invalidate();
        if (baseHttpThreadLocal != null) {
            baseHttpThreadLocal.remove();
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
