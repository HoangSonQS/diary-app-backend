package com.mydiary.api.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserAchievementDto {
    private AchievementDto achievement;
    private LocalDateTime earnedAt;
}