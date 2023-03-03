package com.springboot.app.uberlink.service;

import com.springboot.app.uberlink.exception.exceptions.TokenException;
import com.springboot.app.uberlink.exception.exceptions.UserException;
import com.springboot.app.uberlink.model.ConfirmationToken;
import com.springboot.app.uberlink.model.User;
import com.springboot.app.uberlink.repository.ConfirmationTokenRepository;
import com.springboot.app.uberlink.repository.UserRepository;
import com.springboot.app.uberlink.service.impl.ConfirmationTokenServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfirmationTokenServiceImplTest {

    @InjectMocks
    private ConfirmationTokenServiceImpl confirmationTokenService;

    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Mock
    private UserRepository userRepository;

    private ConfirmationToken confirmationToken;

    private User user;

    @BeforeEach
    void setUp() {

        confirmationTokenService = new ConfirmationTokenServiceImpl(confirmationTokenRepository, userRepository);
        user = new User();

        confirmationToken = new ConfirmationToken(user, "6f7580a0-9e92-4265-9c6e-f5027e032423");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void canCreateConfirmationToken() {

        ConfirmationToken token = confirmationTokenService.createConfirmationToken(user, confirmationToken.getToken());

        assertThat(token.getToken()).isNotNull();
        assertEquals(confirmationToken.getToken(), token.getToken());
        verify(confirmationTokenRepository).save(token);
    }


    @Test
    void canValidateConfirmationToken() {
        String token = confirmationToken.getToken();

        when(confirmationTokenRepository.findByToken(anyString())).thenReturn(confirmationToken);
        String result = confirmationTokenService.validateConfirmationToken(token);

        assertEquals("Valid", result);
    }

    @Test
    void willThrowIfUserIsAlreadyVerifiedWhenValidatingConfirmationToken() {
        String token = confirmationToken.getToken();

        when(confirmationTokenRepository.findByToken(anyString())).thenReturn(confirmationToken);

        user.setEmailVerified(true);

        assertThatThrownBy(() -> confirmationTokenService.validateConfirmationToken(token))
            .isInstanceOf(TokenException.class)
            .hasMessageContaining("Account has already been verified! Please head to login");

        verify(userRepository, never()).save(user);
    }

    @Test
    void willThrowIfConfirmationTokenIsExpiredWhenValidatingConfirmationToken() {
        String token = confirmationToken.getToken();

        when(confirmationTokenRepository.findByToken(anyString())).thenReturn(confirmationToken);

        confirmationToken.setExpirationTime(LocalDateTime.MIN);

        assertThatThrownBy(() -> confirmationTokenService.validateConfirmationToken(token))
                .isInstanceOf(TokenException.class)
                .hasMessageContaining("Link expired. Please click resend to receive a new confirmation link");

        verify(userRepository, never()).save(user);
    }


    @Test
    void canGenerateNewConfirmationToken() {
        when(confirmationTokenRepository.findByToken(anyString())).thenReturn(confirmationToken);

        ConfirmationToken newConfirmationToken = confirmationTokenService.generateNewConfirmationToken("admin@gmail.com");
        assertNotNull(newConfirmationToken);

    }
}