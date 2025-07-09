package com.mydiary.api.service;
import com.mydiary.api.dto.UserAchievementDto;
import com.mydiary.api.entity.User;
import java.util.List;

public interface AchievementService {
    void checkAndGrantAchievements(User user);
    List<UserAchievementDto> getAchievementsForUser(String username);
}