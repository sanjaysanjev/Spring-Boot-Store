package com.sanjay.store.Users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class registerUserRequest {

    @NotBlank(message="Name must not be empty")
    @Size(max=255,message = "Name must of length less than 255 characters")
    @Starting
    private String name;

    @NotBlank(message="Email must not be empty")
    @Email(message = "Valid Email must be provided")
    @Lowercase
    private String email;
    @NotBlank(message="Pasword must not be empty")
    @Size(min=6,max=16,message = "Password must be of length between 6 and 16")
    private String password;
}
