package com.mydiary.api.controller;

import com.mydiary.api.dto.UserAchievementDto;
import com.mydiary.api.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/achievements")
public class AchievementController {

    @Autowired
    private AchievementService achievementService;

    @GetMapping("/my-achievements")
    public ResponseEntity<List<UserAchievementDto>> getMyAchievements(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(achievementService.getAchievementsForUser(username));
    }
}