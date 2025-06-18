package com.mydiary.api.repository;

import com.mydiary.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// JpaRepository<User, Long> nghĩa là Repository này làm việc với Entity User và khóa chính có kiểu Long
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA sẽ tự động hiểu và tạo ra câu lệnh query
    // để tìm một User dựa trên username
    Optional<User> findByUsername(String username);

    // Kiểm tra xem username đã tồn tại chưa
    Boolean existsByUsername(String username);

    // Kiểm tra xem email đã tồn tại chưa
    Boolean existsByEmail(String email);
}