package com.springboot.app.uberlink.service.impl;

import com.springboot.app.uberlink.model.ContactForm;
import com.springboot.app.uberlink.repository.ContactFormRepository;
import com.springboot.app.uberlink.service.ContactFormService;
import com.springboot.app.uberlink.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;

@Service
@AllArgsConstructor
public class ContactFormServiceImpl implements ContactFormService {

    private final ContactFormRepository contactFormRepository;
    private final EmailService emailService;

    @Override
    public void sendContactForm(ContactForm contactForm) {
        emailService.sendContactFormEmail(contactForm);
        contactFormRepository.save(contactForm);
    }
}
