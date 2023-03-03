package com.springboot.app.uberlink.exception.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationExceptionResponse {

    private String authenticationError;
}
