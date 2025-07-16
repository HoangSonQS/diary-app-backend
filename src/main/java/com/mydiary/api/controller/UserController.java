package com.mydiary.api.controller;

import com.mydiary.api.dto.ChangePasswordDto;
import com.mydiary.api.dto.SetPinDto;
import com.mydiary.api.dto.UserProfileDto;
import com.mydiary.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    // API để lấy thông tin profile của người dùng đang đăng nhập
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getUserProfile(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(userService.getUserProfile(username));
    }

    // API để cập nhật thông tin profile
    @PutMapping("/profile")
    public ResponseEntity<UserProfileDto> updateUserProfile(@RequestBody UserProfileDto userProfileDto,
                                                            Authentication authentication) {
        String username = authentication.getName();
        UserProfileDto updatedProfile = userService.updateUserProfile(username, userProfileDto);
        return ResponseEntity.ok(updatedProfile);
    }

    @PostMapping("/set-pin")
    public ResponseEntity<String> setPin(@Valid @RequestBody SetPinDto setPinDto,
                                         Authentication authentication) {
        String username = authentication.getName();
        userService.setPin(username, setPinDto.getPin());
        return ResponseEntity.ok("PIN has been set successfully.");
    }

}