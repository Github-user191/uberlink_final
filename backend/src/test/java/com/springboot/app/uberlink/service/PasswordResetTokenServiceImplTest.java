package com.springboot.app.uberlink.service;


import com.springboot.app.uberlink.exception.exceptions.TokenException;
import com.springboot.app.uberlink.model.PasswordResetToken;
import com.springboot.app.uberlink.model.User;
import com.springboot.app.uberlink.repository.PasswordResetTokenRepository;
import com.springboot.app.uberlink.service.impl.PasswordResetTokenServiceImpl;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetTokenServiceImplTest {

    @InjectMocks
    private PasswordResetTokenServiceImpl passwordResetTokenService;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    private PasswordResetToken passwordResetToken;
    private User user;

    @BeforeEach
    void setUp() {
        passwordResetTokenService = new PasswordResetTokenServiceImpl(passwordResetTokenRepository);
        user = new User(1L, "John Doe", "johndoe@gmail.com", "password");

        passwordResetToken = new PasswordResetToken(user, "6f7580a0-9e92-4265-9c6e-f5027e032423");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void canCreatePasswordResetToken() {
        PasswordResetToken token = passwordResetTokenService.createPasswordResetToken(user, passwordResetToken.getToken());

        assertThat(token.getToken()).isNotNull();
        assertEquals(passwordResetToken.getToken(), token.getToken());
        verify(passwordResetTokenRepository).save(token);

    }

    @Test
    void canValidatePasswordResetToken() {
        String token = passwordResetToken.getToken();

        when(passwordResetTokenRepository.findByToken(anyString())).thenReturn(passwordResetToken);


        String result = passwordResetTokenService.validatePasswordResetToken(token);

        assertEquals("Valid", result);
    }

    @Test
    void willThrowIfPasswordResetTokenIsExpiredWhenValidatingPasswordResetToken() {
        String token = passwordResetToken.getToken();

        when(passwordResetTokenRepository.findByToken(anyString())).thenReturn(passwordResetToken);

        passwordResetToken.setExpirationTime(LocalDateTime.MIN);

        assertThatThrownBy(() -> passwordResetTokenService.validatePasswordResetToken(token))
            .isInstanceOf(TokenException.class)
            .hasMessageContaining("Link has expired. Please generate a new link, you will be redirected shortly.");


        verify(passwordResetTokenRepository, atLeastOnce()).delete(passwordResetToken);
    }

    @Test
    void canGetUserByPasswordResetToken() {

        String token = passwordResetToken.getToken();
        when(passwordResetTokenRepository.findByToken(anyString())).thenReturn(passwordResetToken);


        User foundUser = passwordResetTokenService.getUserByPasswordResetToken(token);

        assertEquals(user, foundUser);
    }


}