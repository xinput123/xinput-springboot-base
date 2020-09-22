package com.xinput.baseboot.handler;

import com.xinput.baseboot.consts.HeaderConsts;
import com.xinput.baseboot.domain.WssManager;
import com.xinput.baseboot.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;

public abstract class AbstractTextWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(AbstractTextWebSocketHandler.class);

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
        final Map<String, Object> attributes = session.getAttributes();
        final Object userId = attributes.get(JwtUtils.AUD);
        final Object platform = attributes.get(JwtUtils.PLATFORM);
        final String sessionKey = platform + "-" + userId;

        WssManager.add(sessionKey, session);
        // 可以在用户建立连接后，立刻推送未读消息，这里省略
    }

    /**
     * 消息传输错误处理
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
        final Map<String, Object> attributes = session.getAttributes();
        final Object userId = attributes.get(JwtUtils.AUD);
        final Object platform = attributes.get(JwtUtils.PLATFORM);
        final String sessionKey = platform + "-" + userId;
        logger.error("用户[{}]在平台[{}]上的长连接[{}]消息传输发生错误.", userId, platform, attributes.get(HeaderConsts.REQUEST_ID_KEY), throwable);
        // 移除并关闭Socket会话
        WssManager.removeAndClose(sessionKey);
    }

    /**
     * 关闭连接后
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        final Map<String, Object> attributes = session.getAttributes();
        final Object userId = attributes.get(JwtUtils.AUD);
        final Object platform = attributes.get(JwtUtils.PLATFORM);
        final String sessionKey = platform + "-" + userId;
        logger.info("用户[{}]在平台[{}]上关闭了长连接[{}].", userId, platform, attributes.get(HeaderConsts.REQUEST_ID_KEY));
        WssManager.remove(sessionKey);
    }
}
