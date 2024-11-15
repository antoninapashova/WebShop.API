package com.example.webshopapi.service.email;

import com.example.webshopapi.dto.EmailBodyDto;
import jakarta.mail.MessagingException;

import java.io.IOException;

public interface MailSenderService {
    void sendNewMail(String subject, EmailBodyDto body) throws MessagingException, IOException;
}