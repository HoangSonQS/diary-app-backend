package com.mydiary.api.service;

import com.mydiary.api.dto.ChangePasswordDto;

public interface UserService {
    void changePassword(String username, ChangePasswordDto changePasswordDto);
}