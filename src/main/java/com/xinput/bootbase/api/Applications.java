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
public class Applications {

    @PassSecure
    @GetMapping("/status")
    public Map<String, Object> status() {
        Map<String, Object> status = Maps.newHashMap();
        status.put("status", "ok");
        status.put("serverTime", LocalDateTime.now());
        status.put("serverName", SpringContentUtils.getId());

        return status;
    }
}
