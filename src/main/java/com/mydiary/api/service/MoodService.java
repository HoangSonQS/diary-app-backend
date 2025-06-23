package com.mydiary.api.service;

import com.mydiary.api.dto.MoodDto;
import java.util.List;

public interface MoodService {
    List<MoodDto> getAllMoods();
}