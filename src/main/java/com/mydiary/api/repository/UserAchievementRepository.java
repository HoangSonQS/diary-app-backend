package com.mydiary.api.repository;

import com.mydiary.api.entity.User;
import com.mydiary.api.entity.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    // Tìm tất cả thành tựu của một user
    List<UserAchievement> findByUser(User user);

    // Kiểm tra xem user đã có thành tựu này chưa
    boolean existsByUserAndAchievement_AchievementKey(User user, String achievementKey);
}