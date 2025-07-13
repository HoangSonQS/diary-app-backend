package com.mydiary.api.service;

import com.mydiary.api.dto.LoginDto;
import com.mydiary.api.dto.RegisterDto;
import com.mydiary.api.entity.User;
import com.mydiary.api.exception.ResourceNotFoundException;
import com.mydiary.api.repository.UserRepository;
import com.mydiary.api.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public String register(RegisterDto registerDto) {

        String usernameToSave = registerDto.getUsername();
        if (usernameToSave == null || usernameToSave.trim().isEmpty()) {
            usernameToSave = registerDto.getEmail();
        }
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new RuntimeException("Email is already taken!");
        }

        User user = new User();
        user.setUsername(usernameToSave);
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        userRepository.save(user);
        return "User registered successfully!";
    }

    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);
        return token;
    }

    @Autowired
    private EmailService emailService;

    @Override
    public void forgotPassword(String email) {
        // Tìm user bằng email
        Optional<User> userOptional = userRepository.findByEmail(email);

        // DÙ TÌM THẤY HAY KHÔNG, KHÔNG NÉM LỖI Ở ĐÂY
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Chỉ thực hiện logic bên trong nếu user tồn tại
            String token = UUID.randomUUID().toString();
            LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(15);

            user.setResetPasswordToken(token);
            user.setResetPasswordTokenExpiry(expiryDate);
            userRepository.save(user);

            String resetLink = "http://localhost:3000/reset-password?token=" + token;
            String emailBody = "Chào bạn,\n\n"
                    + "Bạn đã yêu cầu reset mật khẩu. Vui lòng nhấn vào đường link dưới đây để đặt lại mật khẩu của bạn:\n"
                    + resetLink + "\n\n"
                    + "Đường link sẽ hết hạn trong 15 phút.\n\n"
                    + "Nếu bạn không yêu cầu điều này, xin hãy bỏ qua email này.";

            emailService.sendEmail(user.getEmail(), "Yêu cầu Reset Mật khẩu", emailBody);
        }
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        // Tìm user bằng reset token
        User user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid reset token."));

        // Kiểm tra xem token đã hết hạn chưa
        if (user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Reset token has expired.");
        }

        // Cập nhật mật khẩu mới
        user.setPassword(passwordEncoder.encode(newPassword));

        // Xóa token sau khi đã sử dụng
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiry(null);

        userRepository.save(user);
    }
}