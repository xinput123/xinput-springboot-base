package com.xinput.baseboot.interceptor;

import com.xinput.baseboot.config.BaseBootConfig;
import com.xinput.baseboot.config.SpringContentUtils;
import com.xinput.baseboot.consts.HeaderConsts;
import com.xinput.baseboot.domain.JwtToken;
import com.xinput.baseboot.domain.WssManager;
import com.xinput.baseboot.util.JwtUtils;
import com.xinput.bleach.consts.BaseConsts;
import com.xinput.bleach.util.ObjectId;
import com.xinput.bleach.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * WebSocket 拦截器
 */
@Component
public class BaseBootWebSocketInterceptor implements HandshakeInterceptor {

  private static final Logger logger = LoggerFactory.getLogger(BaseBootWebSocketInterceptor.class);

  /**
   * websocket 握手之前: 验证权限
   */
  @Override
  public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                 WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
    // 在这里决定是否允许链接,http协议转换websoket协议进行前, 握手前Url = {}
    logger.info("websocket握手前URL = [{}].", request.getURI());

    if (!(request instanceof ServletServerHttpRequest)) {
      logger.error("请求类型错误.request=[{}].", request.getClass().toString());
      return false;
    }

    String requestId = ObjectId.stringId();
    String userId = BaseBootConfig.getMockUserId();
    String platform = BaseConsts.DEFAULT;

    if (!BaseConsts.MODE_ACTIVE_DEV.equalsIgnoreCase(SpringContentUtils.getActiveProfile())) {
      HttpServletRequest httpRequest = ((ServletServerHttpRequest) request).getServletRequest();
      String token = httpRequest.getParameter(JwtUtils.TOKEN);
      if (StringUtils.isNullOrEmpty(token)) {
        logger.error("缺少Token参数. URL = [{}].", request.getURI().toString());
        return false;
      }

      try {
        JwtToken jwtToken = JwtUtils.verifyJwtToken(token);
        userId = jwtToken.getAud();
        platform = jwtToken.getPlatform();
      } catch (Exception e) {
        logger.error("长连接解析权限验证失败. token:[{}].", token, e);
        return false;
      }
    }

    String sessionKey = platform + "-" + userId;
    WebSocketSession webSocketSession = WssManager.get(sessionKey);
    if (webSocketSession != null) {
      String existRequestId = String.valueOf(webSocketSession.getAttributes().get(HeaderConsts.REQUEST_ID_KEY));
      logger.info("userId:[{}],已经建立了长连接 [{}]. 检测是否可以复用.", userId, existRequestId);
      // 如果长连接信息存在
      if (webSocketSession.isOpen()) {
        logger.info("userId:[{}] 建立的长连接 [{}] 存在并可以直接复用.", userId, existRequestId);
        attributes.putAll(webSocketSession.getAttributes());
        return true;
      } else {
        logger.info("user:[{}] 已经存在的长连接 [{}] 不可用.重新创建.", userId, existRequestId);
        WssManager.remove(sessionKey);
      }
    }

    attributes.put(JwtUtils.AUD, userId);
    attributes.put(JwtUtils.PLATFORM, platform);
    attributes.put(HeaderConsts.REQUEST_ID_KEY, requestId);
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
