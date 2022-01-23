package com.example.spring.streaming.streamingexample.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@EnableScheduling
@Slf4j
public class AsyncConfig implements AsyncConfigurer
{
    // ============================== [Fields] ==============================

    // -------------------- [Private Fields] --------------------

    private final TaskExecutionProperties taskExecutionProperties;

    // ============================== [Construction / Destruction] ==============================

    // -------------------- [Public Construction / Destruction] --------------------

    public AsyncConfig(TaskExecutionProperties taskExecutionProperties)
    {
        this.taskExecutionProperties = taskExecutionProperties;
    }

    // ============================== [Spring Beans] ==============================

    // -------------------- [Public Spring Beans] --------------------

    // ============================== [Getter/Setter] ==============================

    // -------------------- [Private Getter/Setter] --------------------

    // -------------------- [Public Getter/Setter] --------------------

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler()
    {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

    @Override
    @Bean(name = "taskExecutor")
    public Executor getAsyncExecutor()
    {
        log.info("Create Async Task Executor");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //        executor.setCorePoolSize(taskExecutionProperties.getPool().getCoreSize());
        //        executor.setMaxPoolSize(taskExecutionProperties.getPool().getMaxSize());
        //        executor.setQueueCapacity(taskExecutionProperties.getPool().getQueueCapacity());
        //        executor.setThreadNamePrefix(taskExecutionProperties.getThreadNamePrefix());
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(50);

        return executor;
    }

    @Bean
    protected ConcurrentTaskExecutor getTaskExecutor()
    {
        return new ConcurrentTaskExecutor(this.getAsyncExecutor());
    }

    // ============================== [Methods] ==============================

    // -------------------- [Private Methods] --------------------

    // -------------------- [Public Methods] --------------------

    @Bean
    protected WebMvcConfigurer webMvcConfigurer()
    {
        return new WebMvcConfigurer()
        {
            @Override
            public void configureAsyncSupport(AsyncSupportConfigurer configurer)
            {
                configurer.setTaskExecutor(getTaskExecutor());
            }
        };
    }
}
