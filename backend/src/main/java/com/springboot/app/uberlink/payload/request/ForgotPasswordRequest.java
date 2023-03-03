package com.springboot.app.uberlink.payload.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class ForgotPasswordRequest {
    @NotBlank(message = "Required")
    @Email
    private String forgotPasswordEmail;
    }
