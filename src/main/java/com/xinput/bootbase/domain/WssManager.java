package com.xinput.bootbase.domain;

import com.xinput.bootbase.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocketSession 容器
 */
public class WssManager {

    private static final Logger logger = LoggerFactory.getLogger(WssManager.class);

    /**
     * websocket 会话池
     */
    private static ConcurrentHashMap<String, WebSocketSession> webSocketSessionMap = new ConcurrentHashMap<>();

    /**
     * 添加 websocket 会话
     *
     * @param key
     * @param session
     */
    public static void add(String key, WebSocketSession session) {
        webSocketSessionMap.put(key, session);
    }

    /**
     * 移除 websocket 会话,并将该会话内容返回
     *
     * @param key
     * @return
     */
    public static WebSocketSession remove(String key) {
        return webSocketSessionMap.remove(key);
    }

    /**
     * 删除 websocket,并关闭连接
     *
     * @param key
     */
    public static void removeAndClose(String key) {
        WebSocketSession session = remove(key);
        if (session != null) {
            try {
                session.close();
            } catch (IOException e) {
                logger.error("Websocket session close exception ", e);
            }
        }
    }

    public static ConcurrentHashMap<String, WebSocketSession> getAll() {
        return webSocketSessionMap;
    }

    /**
     * 获取 websocket 会话
     *
     * @param key
     * @return
     */
    public static WebSocketSession get(String key) {
        return webSocketSessionMap.get(key);
    }

    public static String getUserId(WebSocketSession session) {
        Object obj = session.getAttributes().get(JwtUtils.AUD);
        if (obj != null) {
            return String.valueOf(obj);
        }

        return null;
    }

}
