package com.mydiary.api.service;

import com.mydiary.api.dto.AchievementDto;
import com.mydiary.api.dto.UserAchievementDto;
import com.mydiary.api.entity.*;
import com.mydiary.api.repository.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AchievementServiceImpl implements AchievementService {

    @Autowired private UserRepository userRepository;
    @Autowired private EntryRepository entryRepository;
    @Autowired private AchievementRepository achievementRepository;
    @Autowired private UserAchievementRepository userAchievementRepository;
    @Autowired private StreakService streakService; // Dùng lại StreakService

    @Override
    public void checkAndGrantAchievements(User user) {
        checkFirstEntryAchievement(user);
        checkStreakAchievements(user);
        checkTotalEntriesAchievement(user);
    }

    @Override
    public List<UserAchievementDto> getAchievementsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return userAchievementRepository.findByUser(user)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // --- Các hàm kiểm tra private ---

    private void checkFirstEntryAchievement(User user) {
        final String ACHIEVEMENT_KEY = "FIRST_ENTRY";
        if (!userAchievementRepository.existsByUserAndAchievement_AchievementKey(user, ACHIEVEMENT_KEY)) {
            if (entryRepository.countByUser(user) >= 1) {
                grantAchievement(user, ACHIEVEMENT_KEY);
            }
        }
    }

    private void checkTotalEntriesAchievement(User user) {
        final String ACHIEVEMENT_KEY = "WRITE_10_ENTRIES";
        if (!userAchievementRepository.existsByUserAndAchievement_AchievementKey(user, ACHIEVEMENT_KEY)) {
            if (entryRepository.countByUser(user) >= 10) {
                grantAchievement(user, ACHIEVEMENT_KEY);
            }
        }
    }

    private void checkStreakAchievements(User user) {
        final String ACHIEVEMENT_KEY = "STREAK_7_DAYS";
        if (!userAchievementRepository.existsByUserAndAchievement_AchievementKey(user, ACHIEVEMENT_KEY)) {
            int currentStreak = streakService.getCurrentStreak(user.getUsername());
            if (currentStreak >= 7) {
                grantAchievement(user, ACHIEVEMENT_KEY);
            }
        }
    }

    // --- Hàm helper để trao thành tựu ---
    private void grantAchievement(User user, String achievementKey) {
        achievementRepository.findByAchievementKey(achievementKey).ifPresent(achievement -> {
            UserAchievement userAchievement = new UserAchievement();
            userAchievement.setUser(user);
            userAchievement.setAchievement(achievement);
            userAchievementRepository.save(userAchievement);
        });
    }

    // --- Hàm helper để map DTO ---
    private UserAchievementDto mapToDto(UserAchievement userAchievement) {
        AchievementDto achievementDto = new AchievementDto();
        achievementDto.setName(userAchievement.getAchievement().getName());
        achievementDto.setDescription(userAchievement.getAchievement().getDescription());
        achievementDto.setBadgeIcon(userAchievement.getAchievement().getBadgeIcon());

        UserAchievementDto userAchievementDto = new UserAchievementDto();
        userAchievementDto.setAchievement(achievementDto);
        userAchievementDto.setEarnedAt(userAchievement.getEarnedAt());
        return userAchievementDto;
    }
}