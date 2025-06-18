package com.mydiary.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class EntryDto {
    private Long id;

    @NotBlank(message = "Content cannot be blank")
    private String content;

    private LocalDate entryDate;

    private Set<String> tags; // Client sẽ gửi lên một danh sách tên các tag
}