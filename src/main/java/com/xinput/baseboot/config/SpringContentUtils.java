package com.xinput.baseboot.config;

import com.xinput.bleach.consts.BaseConsts;
import com.xinput.bleach.util.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 获取bean工具类
 *
 * @author xinput
 * @date 2020-06-21 10:46
 */
@Component
public class SpringContentUtils implements ApplicationContextAware {

    private static ApplicationContext context = null;

    private static String CURRENT_ACTIVE_PROFILE;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 传入线程中
     *
     * @param beanName
     * @param <T>
     * @return
     */
    public static <T> T getBean(String beanName) {
        return (T) context.getBean(beanName);
    }

    /**
     * 国际化使用
     *
     * @param key
     * @return
     */
    public static String getMessage(String key) {
        return context.getMessage(key, null, Locale.getDefault());
    }

    /**
     * 获取当前环境
     *
     * @return
     */
    public static String getActiveProfile() {
        if (StringUtils.isNotNullOrEmpty(CURRENT_ACTIVE_PROFILE)) {
            return CURRENT_ACTIVE_PROFILE;
        }

        String[] activeProfiles = context.getEnvironment().getActiveProfiles();
        if (activeProfiles == null || activeProfiles.length == 0) {
            CURRENT_ACTIVE_PROFILE = BaseConsts.DEFAULT;
        } else {
            CURRENT_ACTIVE_PROFILE = context.getEnvironment().getActiveProfiles()[0];
        }
        return CURRENT_ACTIVE_PROFILE;
    }

    public static String getId() {
        return context.getId();
    }
}
