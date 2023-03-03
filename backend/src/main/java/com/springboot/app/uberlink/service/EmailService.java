package com.springboot.app.uberlink.service;

import com.springboot.app.uberlink.model.ContactForm;

public interface EmailService {
    void sendContactFormEmail(ContactForm contactForm);
    void sendConfirmationEmail(String email, String confirmationToken);
    void sendResetPasswordEmail(String email, String token);
}
