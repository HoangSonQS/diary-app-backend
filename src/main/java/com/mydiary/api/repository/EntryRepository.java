package com.mydiary.api.repository;

import com.mydiary.api.entity.Entry;
import com.mydiary.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface EntryRepository extends JpaRepository<Entry, Long> {
    // Tìm tất cả các bài viết của một user, sắp xếp theo ngày giảm dần
    List<Entry> findAllByUserOrderByEntryDateDesc(User user);

    // Tìm bài viết của một user vào một ngày cụ thể
    List<Entry> findByUserAndEntryDate(User user, LocalDate date);

    // Tìm tất cả các bài viết của một user có chứa một từ khóa trong nội dung (không phân biệt hoa thường)
    List<Entry> findByUserAndContentContainingIgnoreCaseOrderByEntryDateDesc(User user, String keyword);

    @Query("SELECT e FROM Entry e WHERE e.user = :user AND EXTRACT(MONTH FROM e.entryDate) = :month AND EXTRACT(DAY FROM e.entryDate) = :day ORDER BY e.entryDate DESC")
    List<Entry> findOnThisDay(@Param("user") User user, @Param("month") int month, @Param("day") int day);

    long countByUser(User user);

    Optional<Entry> findByUserAndEntryDateAndIsPrimaryTrue(User user, LocalDate date);
}