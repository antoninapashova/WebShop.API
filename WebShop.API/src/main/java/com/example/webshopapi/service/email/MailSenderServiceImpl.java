package com.example.webshopapi.service.email;

import com.example.webshopapi.entity.SubscriberEntity;
import com.example.webshopapi.repository.SubscriberRepository;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Service
public class MailSenderServiceImpl implements MailSenderService {
    private static final String EMAIL = System.getenv("EMAIL");

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private SubscriberRepository repository;
    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void sendNewMail(String subject, Map<String, Object> templateModel, TemplateEnum template) throws MessagingException, IOException {
        MimeMessage message = createEmail(subject, templateModel, template);
        mailSender.send(message);
    }

    private MimeMessage createEmail(
            String subject,
            Map<String, Object> templateModel,
            TemplateEnum template)
            throws MessagingException {
        MimeMessage email = mailSender.createMimeMessage();
        email.setFrom(new InternetAddress(EMAIL));
        email.setSubject(subject);

        String recipients = collectRecipients();
        email.setRecipients(Message.RecipientType.CC, InternetAddress.parse(recipients));

        String personalizedEmail = formatEmail(templateModel, template);
        email.setContent(personalizedEmail, "text/html; charset=utf-8");

        return email;
    }

    private String collectRecipients() {
        List<SubscriberEntity> recipients = repository.findAll();
        StringJoiner joiner = new StringJoiner(",");
        recipients.forEach(r -> joiner.add(r.getEmail()));
        return joiner.toString();
    }

    private String formatEmail(Map<String, Object> templateModel, TemplateEnum template) {
        Context context = new Context();
        context.setVariables(templateModel);
        return templateEngine.process(template.label, context);
    }
}
