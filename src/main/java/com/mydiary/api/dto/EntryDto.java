package com.mydiary.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntryDto {
    private Long id;

    private String title;

    @NotBlank(message = "Content cannot be blank")
    private String content;

    private LocalDate entryDate;

    private Set<String> tags;

    private Long moodId;

    private List<AttachmentDto> attachments;

    private MoodDto mood;

    private Boolean isPrimary;

}