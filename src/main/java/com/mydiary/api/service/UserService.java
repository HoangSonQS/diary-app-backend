package com.mydiary.api.service;

import com.mydiary.api.dto.ChangePasswordDto;
import com.mydiary.api.dto.UserProfileDto;

public interface UserService {
    void changePassword(String username, ChangePasswordDto changePasswordDto);

    UserProfileDto getUserProfile(String username);
    UserProfileDto updateUserProfile(String username, UserProfileDto userProfileDto);
    void setPin(String username, String pin);
}