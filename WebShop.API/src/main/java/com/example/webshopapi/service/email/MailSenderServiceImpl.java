package com.example.webshopapi.service.email;

import com.example.webshopapi.dto.EmailBodyDto;
import com.example.webshopapi.entity.SubscriberEntity;
import com.example.webshopapi.repository.SubscriberRepository;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;

@Service
public class MailSenderServiceImpl implements MailSenderService {
    private static final String EMAIL = System.getenv("EMAIL");

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private SubscriberRepository repository;

    @Override
    public void sendNewMail(String subject, EmailBodyDto body) throws MessagingException, IOException {
        MimeMessage message = createEmail(subject, body);
        mailSender.send(message);
    }

    private MimeMessage createEmail(
            String subject,
            EmailBodyDto body)
            throws MessagingException {
        MimeMessage email = mailSender.createMimeMessage();
        email.setFrom(new InternetAddress(EMAIL));
        email.setSubject(subject);

        String recipients = collectRecipients();
        email.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));

        String personalizedEmail = formatEmail(body);
        email.setContent(personalizedEmail, "text/html; charset=utf-8");

        return email;
    }

    private String collectRecipients() {
        List<SubscriberEntity> recipients = repository.findAll();
        StringJoiner joiner = new StringJoiner(",");
        recipients.forEach(r -> joiner.add(r.getEmail()));
        return joiner.toString();
    }

    private String formatEmail(EmailBodyDto body) {
        String emailTemplate = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Exclusive Promo Code Just for You!</title>\n" +
                "    <style>\n" +
                "        body { margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4; color: #333; }\n" +
                "        .email-container { width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; }\n" +
                "        .email-header { background-color: #0073e6; padding: 20px; text-align: center; color: #ffffff; }\n" +
                "        .email-header h1 { font-size: 24px; margin: 0; }\n" +
                "        .email-body { padding: 20px; }\n" +
                "        .email-body p { font-size: 16px; line-height: 1.5; margin-bottom: 20px; }\n" +
                "        .promo-code-box { background-color: #ffedcc; padding: 15px; text-align: center; font-size: 24px; font-weight: bold; color: #e67300; border-radius: 4px; margin: 20px 0; }\n" +
                "        .expiration-date { font-size: 14px; color: #888; text-align: center; margin-bottom: 20px; }\n" +
                "        .cta-button { display: inline-block; padding: 12px 25px; background-color: #0073e6; color: #ffffff; text-decoration: none; font-size: 16px; border-radius: 5px; text-align: center; margin: 0 auto; }\n" +
                "        .email-footer { background-color: #f0f0f0; padding: 10px; text-align: center; font-size: 12px; color: #777; }\n" +
                "        @media (max-width: 600px) { .email-container { width: 100%; } }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <table role=\"presentation\" class=\"email-container\">\n" +
                "        <tr>\n" +
                "            <td class=\"email-header\">\n" +
                "                <h1>Exclusive Offer Just for You!</h1>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td class=\"email-body\">\n" +
                "                <p>Hello,</p>\n" +
                "                <p>We’re excited to offer you an exclusive promo code! Use it on your next purchase to save big on the sports gear and accessories you love.</p>\n" +
                "                <div class=\"promo-code-box\">[PROMOCODE]</div>\n" +
                "                <div class=\"expiration-date\">Use by: <strong>[EXPIRATIONDATE]</strong></div>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td class=\"email-footer\">\n" +
                "                <p>© 2024 WebShop. All rights reserved.</p>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</body>\n" +
                "</html>";

        return emailTemplate
                .replace("[PROMOCODE]", body.getPromoCode())
                .replace("[EXPIRATIONDATE]", body.getExpirationDate().toString());
    }
}
