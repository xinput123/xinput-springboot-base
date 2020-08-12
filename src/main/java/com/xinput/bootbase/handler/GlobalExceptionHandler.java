package com.xinput.bootbase.handler;

import com.xinput.bootbase.consts.ErrorCode;
import com.xinput.bootbase.domain.BaseHttp;
import com.xinput.bootbase.domain.Result;
import com.xinput.bootbase.exception.BaseException;
import com.xinput.bootbase.exception.BaseFileException;
import com.xinput.bootbase.exception.BaseUnexpectedException;
import com.xinput.bootbase.util.BuilderUtils;
import com.xinput.bootbase.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * 全局异常捕获
 *
 * @Author: xinput
 * @Date: 2020-06-06 14:32
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private ThreadLocal<BaseHttp> baseHttpThreadLocal;

    /**
     * 参数类型验证错误，http返回状态默认400
     *
     * @param req
     * @param resp
     * @param e
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Result> constraintViolationExceptionHandler(HttpServletRequest req, HttpServletResponse resp, ConstraintViolationException e) {
        logger.error("{} {} ConstraintViolationException. ", req.getMethod(), req.getRequestURI(), e);
        String detailMessage = e.getMessage();
        if (detailMessage.contains(StringUtils.COLON)) {
            detailMessage = detailMessage.substring(detailMessage.indexOf(StringUtils.COLON) + 1);
        }
        Result result = BuilderUtils.of(Result::new)
                .with(Result::setCode, ErrorCode.CLIENT_FORMAT_ERROR)
                .with(Result::setMessage, detailMessage)
                .build();

        clear(req);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(result);
    }

    /**
     * 方法内参数验证异常
     *
     * @param req
     * @param resp
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Result> methodArgumentNotValidException(HttpServletRequest req, HttpServletResponse resp, MethodArgumentNotValidException e) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        StringBuffer errorMsg = new StringBuffer();
        errors.stream().forEach(x -> errorMsg.append(x.getDefaultMessage()).append(";"));
        Result result = BuilderUtils.of(Result::new)
                .with(Result::setCode, ErrorCode.CLIENT_FORMAT_ERROR)
                .with(Result::setMessage, errorMsg.toString())
                .build();

        clear(req);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(result);
    }

    /**
     * 调用方在以post json方式请求服务时，没有对参数进行json序列化所抛出的异常
     * 即： Controller中的方法有 @ResponseBody 参数验证时，如果传入的是个空数据则会抛出这个异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<Result> httpMessageNotReadableExceptionHandler(HttpServletRequest req, HttpServletResponse resp, HttpMessageNotReadableException e) {
        String detailMessage = e.getMessage();
        if (detailMessage.contains(StringUtils.COLON)) {
            detailMessage = detailMessage.substring(0, detailMessage.indexOf(StringUtils.COLON));
        }
        Result result = BuilderUtils.of(Result::new)
                .with(Result::setCode, ErrorCode.CLIENT_FORMAT_ERROR)
                .with(Result::setMessage, detailMessage)
                .build();

        clear(req);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(result);
    }

    /**
     * 空指针异常
     *
     * @param req
     * @param resp
     * @param npe
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<Result> constraintViolationExceptionHandler(HttpServletRequest req, HttpServletResponse resp, NullPointerException npe) {
        logger.error("{} {} NullPointerException.", req.getMethod(), req.getRequestURI(), npe);
        String detailMessage = npe.getStackTrace()[0].toString();

        Result result = BuilderUtils.of(Result::new)
                .with(Result::setCode, ErrorCode.SERVER_INTERNAL_ERROR)
                .with(Result::setMessage, detailMessage)
                .build();

        clear(req);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(result);
    }

    @ExceptionHandler(value = BaseFileException.class)
    public ResponseEntity<Object> baseFileExceptionHandler(HttpServletRequest req, HttpServletResponse resp, BaseFileException bfe) {
        clear(req);
        ResponseEntity<Object> responseEntity = ResponseEntity.ok()
                .contentLength(bfe.getSize())
                .body(bfe.getResource());
        return responseEntity;
    }

    /**
     * 自己封装的基础异常
     *
     * @param req
     * @param resp
     * @param be   封装的异常
     * @return
     */
    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity<Result> baseExceptionHandler(HttpServletRequest req, HttpServletResponse resp, BaseException be) {
        ResponseEntity responseEntity;
        switch (be.getHttpStatus()) {
            case OK:
                Object msg = be.getMsg();
                if (msg == null) {
                    responseEntity = ResponseEntity.status(HttpStatus.OK).build();
                } else {
                    responseEntity = ResponseEntity.status(HttpStatus.OK).body(be.getMsg());
                }
                break;
            case CREATED:
            case NO_CONTENT:
                responseEntity = ResponseEntity.status(be.getHttpStatus()).body(be.getMsg());
                break;
            default:
                Result result = BuilderUtils.of(Result::new)
                        .with(Result::setCode, be.getCode())
                        .with(Result::setMessage, be.getMsg())
                        .build();
                responseEntity = ResponseEntity.status(be.getHttpStatus()).body(result);
                break;
        }

        clear(req);
        return responseEntity;
    }

    @ExceptionHandler(value = BaseUnexpectedException.class)
    public ResponseEntity<Result> baseUnexpectedExceptionHandler(HttpServletRequest req, HttpServletResponse resp, BaseUnexpectedException une) {
        logger.error("request-id:[{}] request:[{}:{}]", req.getAttribute("X-Request-Id"), req.getMethod(), req.getRequestURI(), une);
        return global(req, une.getMessage());
    }

    /**
     * 所有Exception的异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Result> exceptionHandler(HttpServletRequest req, Exception e) {
        logger.error("request-id:[{}] request:[{}:{}]", req.getAttribute("X-Request-Id"), req.getMethod(), req.getRequestURI(), e);
        return global(req, e.getMessage());
    }

    /**
     * 所有Error的异常
     *
     * @param req
     * @param error
     * @return
     */
    @ExceptionHandler(value = Error.class)
    public ResponseEntity<Result> errorHandler(HttpServletRequest req, Error error) {
        logger.error("request-id:[{}] request:[{}:{}]", req.getAttribute("X-Request-Id"), req.getMethod(), req.getRequestURI(), error);
        return global(req, error.getMessage());
    }

    /**
     * 最高等级的Throwable异常
     *
     * @param req
     * @param throwable
     * @return
     */
    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<Result> throwableHandler(HttpServletRequest req, Throwable throwable) {
        logger.error("request-id:[{}] request:[{}:{}]", req.getAttribute("X-Request-Id"), req.getMethod(), req.getRequestURI(), throwable);
        return global(req, throwable.getMessage());
    }

    private ResponseEntity<Result> global(HttpServletRequest req, String errorMsg) {
        Result result = BuilderUtils.of(Result::new)
                .with(Result::setCode, ErrorCode.SERVER_INTERNAL_ERROR)
                .with(Result::setMessage, errorMsg)
                .build();

        clear(req);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * 资源释放
     *
     * @param req
     */
    private void clear(HttpServletRequest req) {
        if (req != null && req.getSession() != null) {
            req.getSession().invalidate();
        }

        if (baseHttpThreadLocal != null) {
            baseHttpThreadLocal.remove();
        }
    }

}
