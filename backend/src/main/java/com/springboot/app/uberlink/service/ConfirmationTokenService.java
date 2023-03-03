package com.springboot.app.uberlink.service;

import com.springboot.app.uberlink.model.ConfirmationToken;
import com.springboot.app.uberlink.model.User;

public interface ConfirmationTokenService {
    ConfirmationToken createConfirmationToken(User user, String token);
    String validateConfirmationToken(String token);
    ConfirmationToken generateNewConfirmationToken(String expiredToken);
}
