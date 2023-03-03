package com.springboot.app.uberlink.service.impl;

import com.springboot.app.uberlink.model.ContactForm;
import com.springboot.app.uberlink.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    @Async
    public void sendContactFormEmail(ContactForm contactForm) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("b777jpuprhvtre@gmail.com");
        mailMessage.setFrom(contactForm.getEmailAddress());
        mailMessage.setSubject("Uberlink");
        mailMessage.setText(contactForm.getMessage());
        mailSender.send(mailMessage);
    }


    @Async
    @Override
    public void sendResetPasswordEmail(String email, String appUrl) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Reset password");
        mailMessage.setText("To reset your password, please click here : " + appUrl + " Note: This link will expire after 24 hours.");
        mailSender.send(mailMessage);
    }


    @Override
    @Async
    public void sendConfirmationEmail(String email, String appUrl) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Account Activation!");
        mailMessage.setText("To confirm your account, please click here : " + appUrl + " Note: This link will expire after 24 hours.");
        mailSender.send(mailMessage);



    }

}
