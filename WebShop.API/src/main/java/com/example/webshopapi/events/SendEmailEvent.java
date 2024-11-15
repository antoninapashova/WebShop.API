package com.example.webshopapi.events;

import com.example.webshopapi.dto.EmailBodyDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class SendEmailEvent extends ApplicationEvent {
    private String subject;
    private EmailBodyDto emailBodyDto;

    public SendEmailEvent(Object source, String subject, EmailBodyDto emailBodyDto) {
        super(source);
        this.subject = subject;
        this.emailBodyDto = emailBodyDto;
    }
}
