package com.bootbase.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.time.LocalDateTime;

/**
 * @Author: xinput
 * @Date: 2020-06-05 10:41
 */
public class TerminateBean {

    private static final Logger logger = LoggerFactory.getLogger(TerminateBean.class);

    @PreDestroy
    public void preDestroy() {
        logger.info("TerminalBean is destroyed..." + LocalDateTime.now().toString());
    }

}
