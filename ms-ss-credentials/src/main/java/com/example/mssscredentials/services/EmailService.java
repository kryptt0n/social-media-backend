package com.example.mssscredentials.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final String FRONT_END_URL;

    public EmailService(JavaMailSender mailSender,
                        @Value("${front-end-url}") String frontEndUrl) {
        this.mailSender = mailSender;
        FRONT_END_URL = frontEndUrl;
    }

    public void sendForgotPasswordCode(String to, String code) {
        sendEmail(to,
                "Reset password",
                "To reset password for social media system use following link: " +
                        FRONT_END_URL + "/reset-password?token=" + code);
    }


    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

}
