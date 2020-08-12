package com.xinput.bootbase.handler;

import com.xinput.bootbase.config.DefaultConfig;
import com.xinput.bootbase.config.SpringContentUtils;
import com.xinput.bootbase.consts.BaseConsts;
import com.xinput.bootbase.domain.WssManager;
import com.xinput.bootbase.util.JwtUtils;
import com.xinput.bootbase.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
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

        Object token = session.getAttributes().get(TOKEN_FIELD);
        if (Objects.isNull(token)) {
            logger.error("非测试环境，webSocket连接必须带有token属性");
            throw new RuntimeException("长连接没有token");
        }

        final Map<String, Object> claims = JwtUtils.verify(String.valueOf(token));
        String userId = String.valueOf(claims.getOrDefault("aud", StringUtils.EMPTY));
        // 用户连接成功,缓存用户会话
        logger.debug("用户[ {} ]创建连接", token);
        session.getAttributes().put("userId", userId);
        WssManager.add(userId, session);
    }

    @Override
    protected abstract void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception;

    /**
     * 接收客户端消息
     * handleTextMessage() 方法是在客户端向服务端发送文本消息的时候触发，
     * 等价于 socket 的原生注解 @OnMessage 。Spring 封装的还有 handleMessage() 方法，
     * 所有客户端发送消息都会触发该方法，无论什么类型的数据。
     */
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        // 读取客户端消息
//    }

    /**
     * afterConnectionClosed() 方法是在客户端(请求)断开连接的时候触发，
     * 等价于 socket 的原生注解 @OnClose
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
            return;
        }

        Object userId = session.getAttributes().get(TOKEN_FIELD);
        if (Objects.nonNull(userId)) {
            if (logger.isDebugEnabled()) {
                logger.debug("用户 [{}] 断开连接", userId);
            }
            WssManager.remove(String.valueOf(userId));
        }
    }
}
