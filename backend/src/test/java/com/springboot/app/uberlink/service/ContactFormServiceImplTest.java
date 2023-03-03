package com.springboot.app.uberlink.service;


import com.springboot.app.uberlink.model.ContactForm;
import com.springboot.app.uberlink.model.User;
import com.springboot.app.uberlink.repository.ContactFormRepository;
import com.springboot.app.uberlink.repository.UserRepository;
import com.springboot.app.uberlink.service.impl.ContactFormServiceImpl;
import com.springboot.app.uberlink.service.impl.EmailServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactFormServiceImplTest {

    @InjectMocks
    private ContactFormServiceImpl contactFormService;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ContactFormRepository contactFormRepository;

    private ContactForm contactForm;

    private User user;

    @BeforeEach
    void setUp() {
        contactFormService = new ContactFormServiceImpl(contactFormRepository, emailService);
        contactForm = new ContactForm(1L, "John Doe", "johndoe@gmail.com", "This is my contact message");
        user = new User(1L, "John Doe", "johndoe@gmail.com", "password");
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    @Disabled
    void canCreateContactForm() {
        when(contactFormRepository.save(any(ContactForm.class))).thenReturn(contactForm);

        contactFormService.sendContactForm(contactForm);

        verify(contactFormRepository).save(any(ContactForm.class));
    }

}
