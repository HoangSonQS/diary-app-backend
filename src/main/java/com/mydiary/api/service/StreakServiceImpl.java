package com.mydiary.api.service;

import com.mydiary.api.entity.User;
import com.mydiary.api.repository.EntryRepository;
import com.mydiary.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class StreakServiceImpl implements StreakService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntryRepository entryRepository;

    @Override
    public int getCurrentStreak(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        int streakCount = 0;
        LocalDate today = LocalDate.now();

        LocalDate dateToTest = today;
        if(entryRepository.findByUserAndEntryDate(user, today).isEmpty()) {
            dateToTest = today.minusDays(1);
        }

        while (entryRepository.findByUserAndEntryDate(user, dateToTest).isPresent()) {
            streakCount++;
            dateToTest = dateToTest.minusDays(1);
        }

        return streakCount;
    }
}
