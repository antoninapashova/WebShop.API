package com.example.webshopapi.jobs;

import com.example.webshopapi.service.PromotionService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeactivatePromotionJob implements Job {

    @Autowired
    private PromotionService promotionService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        promotionService.deactivateExpiredPromotions();
    }
}
