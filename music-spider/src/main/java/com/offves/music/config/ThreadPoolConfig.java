package com.offves.music.config;

import com.offves.music.common.Constant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {

    @Bean(Constant.SONG_TASK_EXECUTOR_NAME)
    public ThreadPoolTaskExecutor songTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(6);
        executor.setMaxPoolSize(60);
        executor.setQueueCapacity(300000);
        executor.setThreadNamePrefix(Constant.SONG_TASK_EXECUTOR_NAME);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}
