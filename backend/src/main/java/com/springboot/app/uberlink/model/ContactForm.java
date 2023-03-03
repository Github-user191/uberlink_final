package com.springboot.app.uberlink.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContactForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Required")
    private String fullName;
    @NotBlank(message = "Required")
    @Email(message = "Email is invalid")
    private String emailAddress;
    @NotBlank(message = "Required")
    @Size(min = 20, message = "Minimum of 20 characters")
    private String message;




}
