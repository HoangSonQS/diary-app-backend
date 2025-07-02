package com.mydiary.api.controller;

import com.mydiary.api.dto.ChangePasswordDto;
import com.mydiary.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user") // Các API liên quan đến user sẽ có tiền tố này
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto,
                                                 Authentication authentication) {
        String username = authentication.getName();
        userService.changePassword(username, changePasswordDto);
        return ResponseEntity.ok("Password changed successfully!");
    }
}