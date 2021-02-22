package com.xinput.baseboot.api;

import com.google.common.collect.Maps;
import com.xinput.baseboot.annotation.PassSecure;
import com.xinput.baseboot.config.SpringContentUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 用于测试服务是否宕机，可用于心跳检测
 *
 * @author xinput
 * @date 2020-06-18 22:03
 */
@RestController
public class BaseBootApps {

  @PassSecure
  @GetMapping("/status")
  public Map<String, Object> status() {
    Map<String, Object> params = Maps.newHashMap();
    params.put("status", "ok");
    params.put("mode", SpringContentUtils.getActiveProfile());
    params.put("serverName", SpringContentUtils.getId());
    params.put("serverTime", LocalDateTime.now());

    return params;
  }
}
