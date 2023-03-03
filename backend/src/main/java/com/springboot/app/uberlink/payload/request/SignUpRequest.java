package com.springboot.app.uberlink.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    private String fullName;
    private String emailAddress;
    private String mobileNumber;
    private String password;
    private String confirmPassword;
}
