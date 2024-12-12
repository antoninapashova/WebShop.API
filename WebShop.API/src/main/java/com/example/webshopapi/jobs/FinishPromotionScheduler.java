package com.example.webshopapi.jobs;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FinishPromotionScheduler {

    @Bean
    JobDetail deactivatePromotionsJobDetail(){
         return JobBuilder.newJob(DeactivatePromotionJob.class)
                 .withIdentity("deactivatePromotionJob")
                 .storeDurably()
                 .build();
    }

    @Bean
    Trigger deactivatePromotionsTrigger(JobDetail deactivatePromotionsJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(deactivatePromotionsJobDetail)
                .withIdentity("deactivatePromotionJob")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(24).repeatForever())
                .build();
    }
}
