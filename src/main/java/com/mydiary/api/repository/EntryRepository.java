package com.mydiary.api.repository;

import com.mydiary.api.entity.Entry;
import com.mydiary.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EntryRepository extends JpaRepository<Entry, Long> {
    // Tìm tất cả các bài viết của một user, sắp xếp theo ngày giảm dần
    List<Entry> findAllByUserOrderByEntryDateDesc(User user);

    // Tìm bài viết của một user vào một ngày cụ thể
    Optional<Entry> findByUserAndEntryDate(User user, LocalDate date);
}