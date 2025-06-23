package com.mydiary.api.dto;

import lombok.Data;

@Data
public class MoodDto {
    private Long id;
    private String name;
    private String iconName;
}