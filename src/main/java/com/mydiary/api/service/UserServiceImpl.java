package com.mydiary.api.service;

import com.mydiary.api.dto.ChangePasswordDto;
import com.mydiary.api.dto.UserProfileDto;
import com.mydiary.api.entity.User;
import com.mydiary.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void changePassword(String username, ChangePasswordDto changePasswordDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
            throw new IllegalStateException("Incorrect old password");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));

        userRepository.save(user);
    }

    @Override
    public UserProfileDto getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return mapUserToProfileDto(user);
    }

    @Override
    public UserProfileDto updateUserProfile(String username, UserProfileDto userProfileDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Cập nhật các trường được phép thay đổi
        user.setBio(userProfileDto.getBio());
        user.setAvatarUrl(userProfileDto.getAvatarUrl());

        User updatedUser = userRepository.save(user);
        return mapUserToProfileDto(updatedUser);
    }

    // Hàm helper để chuyển đổi từ User Entity sang UserProfileDto
    private UserProfileDto mapUserToProfileDto(User user) {
        UserProfileDto dto = new UserProfileDto();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setBio(user.getBio());
        return dto;
    }

    @Override
    public void setPin(String username, String pin) {
        // 1. Tìm người dùng trong database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // 2. Băm (hash) mã PIN mới bằng chính PasswordEncoder đã có
        String hashedPin = passwordEncoder.encode(pin);

        // 3. Cập nhật trường pinHash và lưu lại
        user.setPinHash(hashedPin);
        userRepository.save(user);
    }
}