package com.xinput.bootbase.api;

import com.google.common.collect.Maps;
import com.xinput.bootbase.annotation.PassSecure;
import com.xinput.bootbase.config.SpringContentUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author xinput
 * @date 2020-06-18 22:03
 */
@RestController
public class BootBaseApps {

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
