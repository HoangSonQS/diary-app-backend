package com.mydiary.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PinLoginDto {

    @NotBlank(message = "Username or email is required")
    private String usernameOrEmail;

    @NotBlank(message = "PIN is required")
    private String pin;
}