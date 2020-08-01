package com.bootbase.config;

import com.bootbase.domain.TerminateBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: xinput
 * @Date: 2020-06-05 10:40
 */
@Configuration
public class ShutDownConfig {

    @Bean
    public TerminateBean getTerminateBean() {
        return new TerminateBean();
    }

}
