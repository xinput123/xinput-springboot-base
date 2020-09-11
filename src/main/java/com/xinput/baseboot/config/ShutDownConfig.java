package com.xinput.baseboot.config;

import com.xinput.baseboot.domain.TerminateBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xinput
 * @date 2020-06-05 10:40
 */
@Configuration
public class ShutDownConfig {

    @Bean
    public TerminateBean getTerminateBean() {
        return new TerminateBean();
    }

}
