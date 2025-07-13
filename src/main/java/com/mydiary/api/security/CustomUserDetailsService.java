package com.mydiary.api.security;

import com.mydiary.api.entity.User;
import com.mydiary.api.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

/**
 * Lớp này chịu trách nhiệm tải thông tin chi tiết của người dùng từ database
 * để Spring Security có thể thực hiện việc xác thực.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Sử dụng constructor injection, đây là một thực hành tốt.
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Phương thức này được Spring Security gọi trong quá trình xác thực.
     * Nó nhận vào một chuỗi có thể là username hoặc email và tìm kiếm người dùng tương ứng.
     *
     * @param usernameOrEmail Chuỗi định danh người dùng (username hoặc email).
     * @return Một đối tượng UserDetails chứa thông tin cần thiết cho việc xác thực.
     * @throws UsernameNotFoundException nếu không tìm thấy người dùng nào.
     */
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // --- LOGIC CẬP NHẬT ---
        // 1. Thử tìm người dùng bằng username trước.
        // 2. Nếu không tìm thấy, orElseGet sẽ được thực thi, và nó sẽ thử tìm bằng email.
        // 3. Nếu cả hai đều không tìm thấy, orElseThrow sẽ ném ra exception.
        User user = userRepository.findByUsername(usernameOrEmail)
                .orElseGet(() -> userRepository.findByEmail(usernameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với: " + usernameOrEmail)));
        // -----------------------

        // Tạo một Set chứa các quyền hạn của người dùng.
        // Hiện tại, chúng ta gán mặc định quyền "ROLE_USER" cho tất cả.
        Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        // Trả về một đối tượng UserDetails mà Spring Security có thể hiểu được.
        // Nó bao gồm username (luôn là username duy nhất, không phải email),
        // mật khẩu đã mã hóa, và danh sách quyền hạn.
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}