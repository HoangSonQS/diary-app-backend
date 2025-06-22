package com.mydiary.api.service;

import com.mydiary.api.dto.MoodDto;
import com.mydiary.api.entity.Mood;
import com.mydiary.api.repository.MoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MoodServiceImpl implements MoodService {

    @Autowired
    private MoodRepository moodRepository;

    @Override
    public List<MoodDto> getAllMoods() {
        List<Mood> moods = moodRepository.findAll();
        return moods.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private MoodDto mapToDto(Mood mood) {
        MoodDto moodDto = new MoodDto();
        moodDto.setId(mood.getId());
        moodDto.setName(mood.getName());
        moodDto.setIconName(mood.getIconName());
        return moodDto;
    }
}