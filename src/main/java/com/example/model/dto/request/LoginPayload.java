package com.example.model.dto.request;

import com.example.annotation.ValidEmail;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class LoginPayload {

    @NotEmpty
    @ValidEmail
    private String username;

    @NotEmpty
    @Size(min = 6, max = 32, message = "Password must be between 6 and 32 characters")
    private String password;

}