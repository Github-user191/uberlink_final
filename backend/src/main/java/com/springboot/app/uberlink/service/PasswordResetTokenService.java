package com.springboot.app.uberlink.service;

import com.springboot.app.uberlink.model.PasswordResetToken;
import com.springboot.app.uberlink.model.User;

public interface PasswordResetTokenService {
    PasswordResetToken createPasswordResetToken(User user, String token);
    String validatePasswordResetToken(String token);
    User getUserByPasswordResetToken(String token);
}
