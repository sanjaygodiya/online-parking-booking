package com.demo.parkingmanagementsystem.service;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    
    /** 
     * @param to
     * @param subject
     * @param text
     */
    public void sendSimpleMessage(String to, String subject, String text) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        try {
            helper.setText(text, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("no-reply@gmail.com", "BookMyParking");
            emailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }
}
