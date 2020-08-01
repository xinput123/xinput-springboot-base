package com.bootbase.config;

import com.bootbase.domain.BaseHttp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wangdongpeng
 * @Date: 2020-06-09 13:59
 * @Description
 * @Version 1.0
 */
@Configuration
public class ThreadLocalConfig {

    @Bean
    public ThreadLocal<BaseHttp> getThreadLocal() {
        return new ThreadLocal();
    }

}
