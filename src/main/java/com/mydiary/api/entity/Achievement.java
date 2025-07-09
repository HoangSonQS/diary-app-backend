package com.mydiary.api.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "achievements")
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tên của thành tựu, ví dụ: "Streak 7 ngày"
    @Column(nullable = false, unique = true)
    private String name;

    // Mô tả chi tiết về thành tựu
    @Column(nullable = false)
    private String description;

    // Tên icon của huy hiệu
    @Column(name = "badge_icon")
    private String badgeIcon;

    // Một "key" định danh duy nhất để code dễ dàng tham chiếu
    // Ví dụ: "STREAK_7_DAYS", "WRITE_50_ENTRIES"
    @Column(name = "achievement_key", nullable = false, unique = true)
    private String achievementKey;
}