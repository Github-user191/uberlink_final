package com.springboot.app.uberlink.event.listener;


import com.springboot.app.uberlink.event.RegistrationCompleteEvent;
import com.springboot.app.uberlink.model.User;
import com.springboot.app.uberlink.service.ConfirmationTokenService;
import com.springboot.app.uberlink.service.EmailService;
import com.springboot.app.uberlink.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {


    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;


    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // Create confirmation token for user
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        confirmationTokenService.createConfirmationToken(user, token);

        String url = "https://uberlink.online/confirm-email/" + token;
        emailService.sendConfirmationEmail(user.getEmailAddress(), url);

        //log.info("Click the link to verify your account: {}", url);
    }
}
