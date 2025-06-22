package com.mydiary.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntryDto {
    private Long id;

    @NotBlank(message = "Content cannot be blank")
    private String content;

    private LocalDate entryDate;
    private Set<String> tags;

    // Dùng để NHẬN DỮ LIỆU từ client khi tạo/sửa
    private Long moodId;

    // Dùng để TRẢ DỮ LIỆU về cho client khi xem
    private MoodDto mood;
}