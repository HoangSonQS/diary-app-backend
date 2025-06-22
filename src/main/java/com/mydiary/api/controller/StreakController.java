package com.mydiary.api.controller;

import com.mydiary.api.service.StreakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/streaks")
public class StreakController {
    @Autowired
    private StreakService streakService;

    @GetMapping("/current")
    public ResponseEntity<Map<String, Integer>> getCurrentStreak(Authentication authentication) {
        String username = authentication.getName();
        int streakCount = streakService.getCurrentStreak(username);

        Map<String, Integer> response = Collections.singletonMap("streakCount", streakCount);

        return ResponseEntity.ok(response);
    }
}
