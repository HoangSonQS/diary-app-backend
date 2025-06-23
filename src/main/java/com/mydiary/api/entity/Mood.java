package com.mydiary.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "moods")
public class Mood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "icon_name")
    private String iconName;

    @OneToMany(mappedBy = "mood")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Entry> entries = new HashSet<>();
}