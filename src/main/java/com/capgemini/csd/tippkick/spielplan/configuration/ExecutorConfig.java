package com.capgemini.csd.tippkick.spielplan.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class ExecutorConfig {

    private static final int POOL_SIZE = 5;
    private static final String THREAD_NAME_PREFIX = "ThreadPoolTaskScheduler";

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(POOL_SIZE);
        threadPoolTaskScheduler.setThreadNamePrefix(
                THREAD_NAME_PREFIX);
        return threadPoolTaskScheduler;
    }
}
