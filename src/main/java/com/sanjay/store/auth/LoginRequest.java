package com.sanjay.store.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "email is required")
    @Email(message = "not a valid mail id")
    private String email;
    @NotBlank(message = "password is required")
    private String password;
}
