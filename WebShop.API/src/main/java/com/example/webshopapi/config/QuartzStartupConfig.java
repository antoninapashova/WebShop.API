package com.example.webshopapi.config;

import org.quartz.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzStartupConfig {

    private final Scheduler scheduler;

    public QuartzStartupConfig(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Bean
    public QuartzStartupConfig startScheduler() {
        try {
            if (!scheduler.isStarted()) {
                scheduler.start();
            }
            return this;
        } catch (Exception e) {
            throw new RuntimeException("Failed to start Quartz Scheduler", e);
        }
    }
}