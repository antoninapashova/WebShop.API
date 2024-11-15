package com.example.webshopapi.service.eventHandlers;

import com.example.webshopapi.events.SendEmailEvent;
import com.example.webshopapi.service.email.MailSenderService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.IOException;

@Component
public class MailSenderServiceHandler {
    @Autowired
    private MailSenderService mailSenderService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void processEmail(SendEmailEvent sendEmailEvent) throws MessagingException, IOException {
       mailSenderService.sendNewMail(sendEmailEvent.getSubject(), sendEmailEvent.getEmailBodyDto());
    }
}
