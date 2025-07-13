package com.mydiary.api.service;

import com.mydiary.api.entity.User;
import com.mydiary.api.repository.EntryRepository;
import com.mydiary.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private EmailService emailService; // Dùng lại EmailService đã tạo

    // Annotation ra lệnh cho Spring tự động chạy phương thức này
    // theo lịch đã định trong cron expression.
    @Scheduled(cron = "0 0 20 * * *") // Chạy vào lúc 20:00 (8 giờ tối) mỗi ngày
    @Override
    public void sendDailyReminders() {
        System.out.println("Executing daily reminder job...");

        // 1. Lấy danh sách tất cả người dùng
        List<User> allUsers = userRepository.findAll();

        // 2. Lặp qua từng người dùng
        for (User user : allUsers) {
            // 3. Kiểm tra xem người dùng có viết bài hôm nay không
            boolean hasWrittenToday = entryRepository.findByUserAndEntryDate(user, LocalDate.now()).isPresent();

            if (!hasWrittenToday) {
                // 4. Nếu chưa viết, gửi email nhắc nhở
                System.out.println("Sending reminder to: " + user.getEmail());
                String subject = "Nhắc nhở viết nhật ký hàng ngày";
                String body = "Chào " + user.getUsername() + ",\n\n"
                        + "Đừng quên ghi lại những khoảnh khắc của ngày hôm nay nhé!\n\n"
                        + "Trân trọng,\nỨng dụng Nhật ký của bạn.";

                emailService.sendEmail(user.getEmail(), subject, body);
            }
        }
        System.out.println("Daily reminder job finished.");
    }
}