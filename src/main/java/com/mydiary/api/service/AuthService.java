package com.mydiary.api.service;

import com.mydiary.api.dto.LoginDto;
import com.mydiary.api.dto.RegisterDto;

public interface AuthService {
    String register(RegisterDto registerDto);
    String login(LoginDto loginDto);
}