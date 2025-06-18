// File: src/main/java/com/mydiary/api/controller/AuthController.java
package com.mydiary.api.controller;

import com.mydiary.api.dto.JwtAuthResponseDto;
import com.mydiary.api.dto.LoginDto;
import com.mydiary.api.dto.RegisterDto;
import com.mydiary.api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // API Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDto> login(@Valid @RequestBody LoginDto loginDto){
        String token = authService.login(loginDto);
        JwtAuthResponseDto jwtAuthResponse = new JwtAuthResponseDto();
        jwtAuthResponse.setAccessToken(token);
        return ResponseEntity.ok(jwtAuthResponse);
    }

    // API Đăng ký
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDto registerDto) {
        String response = authService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}