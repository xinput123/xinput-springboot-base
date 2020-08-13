package com.xinput.bootbase.api;

import com.xinput.bootbase.config.DefaultConfig;
import com.xinput.bootbase.consts.ErrorCode;
import com.xinput.bootbase.domain.BaseHttp;
import com.xinput.bootbase.domain.Result;
import com.xinput.bootbase.exception.BaseException;
import com.xinput.bootbase.exception.BaseFileException;
import com.xinput.bootbase.exception.BaseUnexpectedException;
import com.xinput.bootbase.util.MimeTypes;
import com.xinput.bootbase.util.StringUtils;
import com.xinput.bootbase.util.TimeUtils;
import org.apache.commons.codec.net.URLCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

@Service
public abstract class BaseService {

    private static final String INLINE_DISPOSITION_TYPE = "inline";

    private static final String ATTACHMENT_DISPOSITION_TYPE = "attachment";

    private static final URLCodec ENCODER = new URLCodec();

    @Autowired
    private ThreadLocal<BaseHttp> baseHttpThreadLocal;

    /**
     * set jwt token cookie with httpOnly and secure deps on your 'application.session.secure' setting.
     *
     * @param jwt
     * @param duration like 2h, 3d
     */
    protected void setJwtCookie(String jwt, String duration) {
        Cookie cookie = new Cookie(DefaultConfig.getCookieTokenName(), jwt);
        cookie.setPath(StringUtils.SLASH);
        cookie.setSecure(DefaultConfig.getCookieSecure());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(TimeUtils.parseDuration(duration));

        baseHttpThreadLocal.get().getResponsen().addCookie(cookie);
    }

    /**
     * 在线查看
     *
     * @param file
     */
    protected void renderBinary(File file) {
        render(file, null, file.getName(), true);
    }

    /**
     * 下载
     *
     * @param file
     * @param name 别名
     */
    protected void renderBinary(File file, String name) {
        render(file, null, name, false);
    }

    protected void renderBinary(InputStream is) {
        render(null, is, null, true);
    }

    protected void renderBinary(InputStream is, String name) {
        render(null, is, name, false);
    }

    /**
     * @param file
     * @param is
     * @param name
     * @param inline true时表示在线查看，false表示下载
     */
    private void render(File file, InputStream is, String name, boolean inline) {
        HttpServletResponse response = baseHttpThreadLocal.get().getResponsen();
        if (name != null) {
            setContentTypeIfNotSet(response, MimeTypes.getContentType(name));
        }

        try {
            if (StringUtils.isEmpty(response.getHeader(HttpHeaders.CONTENT_DISPOSITION))) {
                addContentDispositionHeader(response, name, inline);
            }
            if (file != null) {
                renderFile(file);
            } else {
                renderInputStream(is, response);
            }
        } catch (Exception e) {

        }
    }

    private static void renderInputStream(InputStream inputStream, HttpServletResponse response) throws IOException {
        OutputStream outputStream = response.getOutputStream();
        // 在http响应中输出流
        byte[] cache = new byte[1024];
        int nRead = 0;
        while ((nRead = inputStream.read(cache)) != -1) {
            outputStream.write(cache, 0, nRead);
            outputStream.flush();
        }
        outputStream.flush();
    }

    private static void renderFile(File file) {
        if (!file.exists()) {
            throw new BaseUnexpectedException("Your file does not exists (" + file + ")");
        }
        if (!file.canRead()) {
            throw new BaseUnexpectedException("Can't read your file (" + file + ")");
        }
        if (!file.isFile()) {
            throw new BaseUnexpectedException("Your file is not a real file (" + file + ")");
        }

        InputStreamResource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new BaseUnexpectedException("Your file does not found (" + file + ")");
        }

        throw new BaseFileException(resource, file.length());
    }

    private void setContentTypeIfNotSet(HttpServletResponse response, String contentType) {
        if (StringUtils.isEmpty(response.getContentType())) {
            response.setContentType(contentType);
        }
    }

    private void addContentDispositionHeader(HttpServletResponse response, String name, boolean inline) throws UnsupportedEncodingException {
        if (name == null) {
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, dispositionType(inline));
        } else if (canAsciiEncode(name)) {
            String contentDisposition = "%s; filename=\"%s\"";
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format(contentDisposition, dispositionType(inline), name));
        } else {
            String encoding = response.getCharacterEncoding();
            String contentDisposition = "%1$s; filename*=" + encoding + "''%2$s; filename=\"%2$s\"";
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format(contentDisposition, dispositionType(inline), ENCODER.encode(name, encoding)));
        }
    }

    private String dispositionType(boolean inline) {
        return inline ? INLINE_DISPOSITION_TYPE : ATTACHMENT_DISPOSITION_TYPE;
    }

    private boolean canAsciiEncode(String string) {
        CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder();
        return asciiEncoder.canEncode(string);
    }

    /**
     * Send 400 bad request
     */
    protected void badRequestIfNull(Object object, String message) {
        if (object == null) {
            badRequest(message);
        }
    }

    /**
     * Send 400 bad request
     */
    protected void badRequest() {
        badRequest("Bad request");
    }

    /**
     * Send 400 bad request
     */
    protected void badRequest(String message) throws BaseException {
        throw new BaseException(HttpStatus.BAD_REQUEST, ErrorCode.CLIENT_RESOURCE_NOT_FOUND, message);
    }

    /**
     * Send 400 bad request
     */
    protected void badRequest(Result result) {
        throw new BaseException(HttpStatus.BAD_REQUEST, result.getCode(), result.getMessage());
    }

    /**
     * Send 401 Unauthorized
     */
    protected void unauthorized(Result result) {
        throw new BaseException(HttpStatus.UNAUTHORIZED, result.getCode(), result.getMessage());
    }

    /**
     * Send 401 Unauthorized
     */
    public void unauthorized(String message) {
        throw new BaseException(HttpStatus.UNAUTHORIZED, ErrorCode.CLIENT_AUTH_ERROR, message);
    }

    /**
     * Send 401 Unauthorized
     */
    public void unauthorized() {
        throw new BaseException(HttpStatus.UNAUTHORIZED, ErrorCode.CLIENT_AUTH_ERROR);
    }

    /**
     * Send 404 bad request
     */
    protected void notFound(String message) {
        throw new BaseException(HttpStatus.NOT_FOUND, ErrorCode.CLIENT_RESOURCE_NOT_FOUND, message);
    }

    /**
     * Send 404 bad request
     */
    protected void notFound() {
        notFound(StringUtils.EMPTY);
    }

    /**
     * Send 404 bad request
     */
    protected void notFoundIfNull(Object o) {
        if (o == null) {
            notFound();
        }
    }

    /**
     * Send 404 bad request
     */
    protected void notFoundIfNull(Object o, String message) {
        if (o == null) {
            notFound(message);
        }
    }

    /**
     * Send 500 INTERNAL_SERVER_ERROR
     */
    protected void error() {
        throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.SERVER_INTERNAL_ERROR);
    }

    /**
     * Send 500 INTERNAL_SERVER_ERROR
     */
    protected void error(String message) {
        throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.SERVER_INTERNAL_ERROR, message);
    }

    /**
     * Send 500 INTERNAL_SERVER_ERROR
     */
    protected void error(Result result) {
        throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, result.getCode(), result.getMessage());
    }

    /**
     * Send 509 CLIENT_OVER_QUOTA
     */
    public void limitExceeded(String message) {
        throw new BaseException(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, ErrorCode.CLIENT_OVER_QUOTA, message);
    }
}
