package com.xinput.bootbase.api;

import com.google.common.collect.Maps;
import com.xinput.bootbase.config.DefaultConfig;
import com.xinput.bootbase.config.SpringContentUtils;
import com.xinput.bootbase.consts.BaseConsts;
import com.xinput.bootbase.consts.ErrorCode;
import com.xinput.bootbase.consts.HeaderConsts;
import com.xinput.bootbase.domain.BaseHttp;
import com.xinput.bootbase.domain.Header;
import com.xinput.bootbase.domain.Result;
import com.xinput.bootbase.exception.BaseException;
import com.xinput.bootbase.exception.BaseFileException;
import com.xinput.bootbase.exception.BaseUnexpectedException;
import com.xinput.bootbase.util.JsonUtils;
import com.xinput.bootbase.util.MimeTypes;
import com.xinput.bootbase.util.StreamUtils;
import com.xinput.bootbase.util.StringUtils;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
import java.util.List;
import java.util.Map;

@Component
public abstract class BaseController {

    private static final String INLINE_DISPOSITION_TYPE = "inline";

    private static final String ATTACHMENT_DISPOSITION_TYPE = "attachment";

    private static final URLCodec encoder = new URLCodec();

    @Autowired
    private ThreadLocal<BaseHttp> baseHttpThreadLocal;

    /**
     * get user id in jwt token.
     *
     * @return
     */
    protected String currentUserId() {
        // 如果是dev环境，直接放行
        if (BaseConsts.MODE_ACTIVE_DEV.equalsIgnoreCase(SpringContentUtils.getActiveProfile())) {
            return DefaultConfig.getMockUserId();
        }

        Object obj = baseHttpThreadLocal.get().getRequest().getAttribute("aud");
        if (obj != null) {
            return String.valueOf(obj);
        }

        return StringUtils.EMPTY;
    }

    /**
     * 在response的header中添加X-Total-Count
     */
    protected void setTotalCount(long totalCount) {
        setTotalCount(HeaderConsts.TOTOL_COUNT_KEY, totalCount);
    }

    /**
     * 在response的header中添加 headerName
     */
    protected void setTotalCount(String headerName, long totalCount) {
        setHeader(headerName, String.valueOf(totalCount));
    }

    /**
     * 在response的header中添加 headerName
     */
    protected void setHeader(String headerName, String value) {
        HttpServletResponse response = baseHttpThreadLocal.get().getResponsen();
        String headerValue = response.getHeader(HeaderConsts.ACCESS_CONTROL_EXPOSE_HEADERS_KEY);
        response.setHeader(HeaderConsts.ACCESS_CONTROL_EXPOSE_HEADERS_KEY, headerValue + StringUtils.COMMA + headerName);

        response.setHeader(headerName, value);
    }

    protected void setHeader(List<Header> headers) {
        HttpServletResponse response = baseHttpThreadLocal.get().getResponsen();
        String headerNames = StreamUtils.union(StreamUtils.collectColumn(headers, Header::getName));
        String headerValue = response.getHeader(HeaderConsts.ACCESS_CONTROL_EXPOSE_HEADERS_KEY);
        response.setHeader(HeaderConsts.ACCESS_CONTROL_EXPOSE_HEADERS_KEY, headerValue + StringUtils.COMMA + headerNames);

        if (CollectionUtils.isEmpty(headers)) {
            headers.forEach(header -> response.setHeader(header.getName(), String.valueOf(header.getValue())));
        }
    }

    /**
     * set total count header to 0 and render empty list.
     */
    protected void renderEmptyList() {
        HttpServletResponse response = baseHttpThreadLocal.get().getResponsen();
        String headerValue = response.getHeader(HeaderConsts.ACCESS_CONTROL_EXPOSE_HEADERS_KEY);
        response.setHeader(HeaderConsts.ACCESS_CONTROL_EXPOSE_HEADERS_KEY, headerValue + StringUtils.COMMA + HeaderConsts.TOTOL_COUNT_KEY);
        response.setHeader(HeaderConsts.TOTOL_COUNT_KEY, "0");
        throw new BaseException(HttpStatus.OK, Lists.newArrayList());
    }

    /**
     * Send a 200 OK response
     */
    protected void ok() {
        throw new BaseException(HttpStatus.OK);
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
            if (StringUtils.isEmpty(response.getHeader(HeaderConsts.CONTENT_DISPOSITION_KEY))) {
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
            response.setHeader(HeaderConsts.CONTENT_DISPOSITION_KEY, dispositionType(inline));
        } else if (canAsciiEncode(name)) {
            String contentDisposition = "%s; filename=\"%s\"";
            response.setHeader(HeaderConsts.CONTENT_DISPOSITION_KEY, String.format(contentDisposition, dispositionType(inline), name));
        } else {
            String encoding = response.getCharacterEncoding();
            String contentDisposition = "%1$s; filename*=" + encoding + "''%2$s; filename=\"%2$s\"";
            response.setHeader(HeaderConsts.CONTENT_DISPOSITION_KEY, String.format(contentDisposition, dispositionType(inline), encoder.encode(name, encoding)));
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
     * Send 201 Created
     */
    protected void created(Object data) {
        throw new BaseException(HttpStatus.CREATED, data);
    }

    /**
     * Send 201 Created
     */
    protected void created(String message) {
        throw new BaseException(HttpStatus.CREATED, message);
    }

    /**
     * Send 204 NO CONTENT
     */
    protected void noContent() {
        throw new BaseException(HttpStatus.NO_CONTENT);
    }

    /**
     * Send 205 NO CONTENT
     */
    protected void resetContent() {
        throw new BaseException(HttpStatus.RESET_CONTENT);
    }

    /**
     * Send 301 redirect
     *
     * @param url
     * @throws IOException
     */
    protected void redirect(String url) {
        HttpServletResponse response = baseHttpThreadLocal.get().getResponsen();
        response.setHeader("Location", url);
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("url", url);
            map.put("code", "resource not exists");
            map.put("request_id", this.baseHttpThreadLocal.get().getRequest().getAttribute(HeaderConsts.REQUEST_ID_KEY));
            throw new BaseException(HttpStatus.NOT_FOUND, ErrorCode.CLIENT_RESOURCE_NOT_FOUND, JsonUtils.toJsonString(map));
        }
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
     * Send 403 forbidden
     */
    public void forbidden() {
        throw new BaseException(HttpStatus.FORBIDDEN, ErrorCode.CLIENT_ACCESS_DENIED);
    }

    /**
     * Send 403 forbidden
     */
    public void forbidden(String message) {
        throw new BaseException(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, ErrorCode.CLIENT_OVER_QUOTA, message);
    }

    /**
     * Send 403 forbidden
     */
    public void forbidden(Result result) {
        throw new BaseException(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, result.getCode(), result.getMessage());

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
}
