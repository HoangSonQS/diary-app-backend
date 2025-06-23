package com.mydiary.api.controller;

import com.mydiary.api.dto.MoodDto;
import com.mydiary.api.service.MoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/moods")
public class MoodController {

    @Autowired
    private MoodService moodService;

    // API này được bảo vệ, chỉ user đã đăng nhập mới có thể xem
    @GetMapping
    public ResponseEntity<List<MoodDto>> getAllMoods() {
        return ResponseEntity.ok(moodService.getAllMoods());
    }
}