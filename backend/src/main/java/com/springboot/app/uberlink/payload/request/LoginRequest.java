package com.springboot.app.uberlink.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Required")
    @Email(message = "Email is invalid")
    private String emailAddress;

    @NotBlank(message = "Required")
    private String password;

}
