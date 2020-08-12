package com.xinput.bootbase.interceptor;

import com.xinput.bootbase.config.SpringContentUtils;
import com.xinput.bootbase.consts.BaseConsts;
import com.xinput.bootbase.util.HttpUtils;
import com.xinput.bootbase.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * WebSocket 拦截器
 */
@Component
public class BaseWebSocketInterceptor implements HandshakeInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(BaseWebSocketInterceptor.class);

    private static final String TOKEN_FIELD = "token";

    /**
     * websocket 握手之前
     *
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @param webSocketHandler
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                                   WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        String urlQuery = serverHttpRequest.getURI().getQuery();
        if (logger.isDebugEnabled()) {
            logger.debug("websocket starts handshaking. urlQuery:[{}].", urlQuery);
        }

        // 如果是dev环境，直接放行
        if (BaseConsts.MODE_ACTIVE_DEV.equalsIgnoreCase(SpringContentUtils.getActiveProfile())) {
            logger.info("the mode is [dev]", urlQuery);
            return true;
        }

        // 获取请求参数
        Map<String, String> paramMap = HttpUtils.decodeParamHashMap(urlQuery);
        String token = paramMap.get(TOKEN_FIELD);
        if (StringUtils.isNullOrEmpty(token)) {
            logger.error("token is empty.");
            return false;
        }

        return true;
    }

    /**
     * websocket 握手之后
     *
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @param webSocketHandler
     * @param e
     */
    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                               WebSocketHandler webSocketHandler, Exception e) {
    }
}
