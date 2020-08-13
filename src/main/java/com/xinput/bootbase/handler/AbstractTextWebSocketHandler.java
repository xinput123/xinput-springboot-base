package com.xinput.bootbase.handler;

import com.xinput.bootbase.config.DefaultConfig;
import com.xinput.bootbase.config.SpringContentUtils;
import com.xinput.bootbase.consts.BaseConsts;
import com.xinput.bootbase.domain.WssManager;
import com.xinput.bootbase.util.HttpUtils;
import com.xinput.bootbase.util.JwtUtils;
import com.xinput.bootbase.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Objects;

public abstract class AbstractTextWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(AbstractTextWebSocketHandler.class);

    protected static final String TOKEN_FIELD = "token";

    /**
     * 握手成功之后
     * <p>
     * afterConnectionEstablished 方法是在 websocket 握手成功之后，创建 socket 连接的时候触发，
     * 等价于 socket 的原生注解 @OnOpen
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if (BaseConsts.MODE_ACTIVE_DEV.equalsIgnoreCase(SpringContentUtils.getActiveProfile())) {
            WssManager.add(DefaultConfig.getMockUserId(), session);
            return;
        }

        Map<String, String> paramMap = HttpUtils.decodeParamHashMap(session.getUri().toString());
        String token = paramMap.getOrDefault("token", StringUtils.EMPTY);
        if (Objects.isNull(token)) {
            logger.error("非测试环境，webSocket连接必须带有token属性");
            throw new RuntimeException("长连接没有token");
        }

        final Map<String, Object> claims = JwtUtils.verify(token);
        String userId = String.valueOf(claims.getOrDefault(JwtUtils.AUD, StringUtils.EMPTY));
        String platform = String.valueOf(claims.getOrDefault(JwtUtils.PLATFORM, BaseConsts.DEFAULT));
        if (logger.isDebugEnabled()) {
            // 用户连接成功,缓存用户会话
            logger.debug("用户[ {} ]创建连接", token);
        }
        session.getAttributes().put(JwtUtils.AUD, userId);
        session.getAttributes().put(JwtUtils.PLATFORM, platform);
        WssManager.add(platform + "-" + userId, session);
    }

    /**
     * afterConnectionClosed() 方法是在客户端(请求)断开连接的时候触发，
     * 等价于 socket 的原生注解 @OnClose
     * <p>
     * 关闭连接后或者发生传输错误时将会调用该方法，尽管会话session可能此时仍然未关闭，
     * 但是不建议在此处给客户端发消息，因为极有可能会发送失败
     *
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if (BaseConsts.MODE_ACTIVE_DEV.equalsIgnoreCase(SpringContentUtils.getActiveProfile())) {
            if (logger.isDebugEnabled()) {
                logger.debug("用户 [{}] 断开连接", DefaultConfig.getMockUserId());
            }
            WssManager.remove(DefaultConfig.getMockUserId());
            session.close();
            return;
        }

        Object userId = session.getAttributes().get(JwtUtils.AUD);
        Object platform = session.getAttributes().getOrDefault(JwtUtils.PLATFORM, BaseConsts.DEFAULT);
        if (Objects.nonNull(userId)) {
            if (logger.isDebugEnabled()) {
                logger.debug("用户 [{}] 断开连接", userId);
            }
            WssManager.remove(platform + "-" + userId);
        }
        session.close();
    }
}
