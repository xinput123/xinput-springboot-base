package com.xinput.bootbase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * websocket服务节点开启
 *
 * @Author: wangdongpeng
 * @Date: 2020-08-07 14:09
 * @Description
 * @Version 1.0
 */
@EnableScheduling
@Configuration
public class WebSocketConfig {

    /**
     * 服务器节点
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}