package com.springboot.app.uberlink.controller;

import com.springboot.app.uberlink.model.ContactForm;
import com.springboot.app.uberlink.service.ContactFormService;
import com.springboot.app.uberlink.service.impl.ContactFormServiceImpl;
import com.springboot.app.uberlink.service.impl.ErrorValidationServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/contact/")
public class ContactFormController {

    private final ErrorValidationServiceImpl errorValidationService;
    private final ContactFormService contactFormService;

    public ContactFormController(ErrorValidationServiceImpl errorValidationService, ContactFormService contactFormService) {
        this.errorValidationService = errorValidationService;
        this.contactFormService = contactFormService;

    }

    @PostMapping("/send")
    public ResponseEntity<?> sendContactForm(@Valid @RequestBody ContactForm contactForm, BindingResult result) {
        ResponseEntity<?> errorMap = errorValidationService.validationService(result);
        if(errorMap != null) return errorMap;


        contactFormService.sendContactForm(contactForm);


        return new ResponseEntity<>("Your query has been sent successfully. Please wait upto 2 days for a response",
                HttpStatus.CREATED);
    }

}
