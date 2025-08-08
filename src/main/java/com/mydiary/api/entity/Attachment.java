package com.mydiary.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "attachments")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Đường dẫn tương đối tới file, ví dụ: /uploads/images/ten-file-duy-nhat.jpg
    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    // Chú thích cho hình ảnh
    @Column
    private String caption;

    // Mối quan hệ: Nhiều Attachment thuộc về một Entry
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entry_id", nullable = false)
    @JsonBackReference // Tránh đệ quy vô hạn khi chuyển thành JSON
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Entry entry;
}
