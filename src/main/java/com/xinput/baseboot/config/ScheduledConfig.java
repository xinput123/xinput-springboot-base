package com.xinput.baseboot.config;

import com.xinput.bleach.consts.BaseConsts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 定时任务会和WebSocket冲突，所以这里创建一个bean
 */
@Configuration
public class ScheduledConfig {

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduling = new ThreadPoolTaskScheduler();
        scheduling.setPoolSize(20);
        scheduling.setThreadNamePrefix(SpringContentUtils.getId());
        scheduling.setThreadGroupName(BaseConsts.DEFAULT);
        scheduling.initialize();
        return scheduling;
    }
}
