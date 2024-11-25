package com.example.webshopapi.events;

import com.example.webshopapi.service.email.TemplateEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

@Getter
@Setter
public class SendEmailEvent extends ApplicationEvent {
    private String subject;
    private Map<String, Object> templateModel;
    private TemplateEnum template;

    public SendEmailEvent(Object source, String subject, Map<String, Object> templateModel, TemplateEnum template) {
        super(source);
        this.subject = subject;
        this.templateModel = templateModel;
        this.template = template;
    }
}
