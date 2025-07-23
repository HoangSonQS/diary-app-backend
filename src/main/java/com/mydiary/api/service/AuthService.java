package com.mydiary.api.service;

import com.mydiary.api.dto.LoginDto;
import com.mydiary.api.dto.PinLoginDto;
import com.mydiary.api.dto.RegisterDto;

public interface AuthService {
    String register(RegisterDto registerDto);
    String login(LoginDto loginDto);
    void forgotPassword(String email);
    void resetPassword(String token, String newPassword);
    String loginWithPin(PinLoginDto pinLoginDto);
    boolean userHasPin(String username);
}