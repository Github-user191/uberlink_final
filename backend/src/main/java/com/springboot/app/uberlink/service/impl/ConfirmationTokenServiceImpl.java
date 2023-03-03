package com.springboot.app.uberlink.service.impl;

import com.springboot.app.uberlink.exception.exceptions.TokenException;
import com.springboot.app.uberlink.model.ConfirmationToken;
import com.springboot.app.uberlink.model.User;
import com.springboot.app.uberlink.repository.ConfirmationTokenRepository;
import com.springboot.app.uberlink.repository.UserRepository;
import com.springboot.app.uberlink.service.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.springboot.app.uberlink.model.ConfirmationToken.EXPIRATION_TIME_HOURS;

@Service
@AllArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserRepository userRepository;

    @Override
    public ConfirmationToken createConfirmationToken(User user, String token) {
        ConfirmationToken confirmationToken = new ConfirmationToken(user, token);
        // Save random UUID confirmation token to DB

        confirmationTokenRepository.save(confirmationToken);
        return confirmationToken;
    }

    @Override
    public String validateConfirmationToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token);

        if(confirmationToken != null) {
            User user = confirmationToken.getUser();

            if(user.isEmailVerified()) {
//            confirmationTokenRepository.delete(confirmationToken);
                throw new TokenException("Account has already been verified! Please head to login");
            }

            if((confirmationToken.isConfirmationTokenExpired())) {
//                confirmationTokenRepository.delete(confirmationToken);
                throw new TokenException("Link expired. Please click resend to receive a new confirmation link");
            }
            user.setEmailVerified(true);
            userRepository.save(user);
        } else {
            throw new TokenException("There was an error. Please check your email for a verification email");
        }

        return "Valid";
    }

    @Override
    public ConfirmationToken generateNewConfirmationToken(String expiredToken) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(expiredToken);


        if(confirmationToken != null) {
            User user = confirmationToken.getUser();

            if(user.isEmailVerified())
                throw new TokenException("Account has already been verified! Please head to login");

            confirmationToken.setToken(UUID.randomUUID().toString());
            confirmationToken.setCreatedAt(LocalDateTime.now());
            confirmationToken.setExpirationTime(LocalDateTime.now().plusHours(EXPIRATION_TIME_HOURS));
            confirmationTokenRepository.save(confirmationToken);
        } else {
            throw new TokenException("There was an error. Please check your email for a verification email");
        }

        return confirmationToken;
    }
}
