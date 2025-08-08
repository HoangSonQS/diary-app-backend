package com.mydiary.api.dto;
import lombok.Data;

@Data
public class AttachmentDto {
    private Long id;
    private String fileUrl;
    private String caption;
}
