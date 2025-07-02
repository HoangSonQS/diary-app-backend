package com.mydiary.api.dto;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class UserProfileDto {
    private String username;
    private String email;

    @URL(message = "Avatar must be a valid URL")
    private String avatarUrl;

    private String bio;
}