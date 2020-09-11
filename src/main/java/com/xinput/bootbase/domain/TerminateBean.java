package com.xinput.bootbase.domain;

import com.xinput.bootbase.config.SpringContentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;

/**
 * @author xinput
 * @date 2020-06-05 10:41
 */
public class TerminateBean {

    private static final Logger logger = LoggerFactory.getLogger(TerminateBean.class);

    @PreDestroy
    public void preDestroy() {
        logger.info("Server [{}] is destroyed...", SpringContentUtils.getId());
    }

}
