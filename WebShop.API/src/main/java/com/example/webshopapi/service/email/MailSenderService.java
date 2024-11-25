package com.example.webshopapi.service.email;

import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.Map;

public interface MailSenderService {
    void sendNewMail(String subject, Map<String, Object> templateModel, TemplateEnum template) throws MessagingException, IOException;
}