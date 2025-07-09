package com.mydiary.api;

import com.mydiary.api.entity.Achievement;
import com.mydiary.api.repository.AchievementRepository;
import com.mydiary.api.entity.Mood;
import com.mydiary.api.repository.MoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private MoodRepository moodRepository;

    @Autowired
    private AchievementRepository achievementRepository;

    @Override
    public void run(String... args) throws Exception {
        if (moodRepository.count() == 0) {
            System.out.println("Seeding moods data...");

            Mood happy = new Mood();
            happy.setName("Vui vẻ");
            happy.setIconName("sentiment_very_satisfied");

            Mood sad = new Mood();
            sad.setName("Buồn");
            sad.setIconName("sentiment_very_dissatisfied");

            Mood neutral = new Mood();
            neutral.setName("Bình thường");
            neutral.setIconName("sentiment_neutral");

            Mood excited = new Mood();
            excited.setName("Hào hứng");
            excited.setIconName("celebration");

            Mood angry = new Mood();
            angry.setName("Giận dữ");
            angry.setIconName("sentiment_angry");

            moodRepository.save(happy);
            moodRepository.save(sad);
            moodRepository.save(neutral);
            moodRepository.save(excited);
            moodRepository.save(angry);

            System.out.println("Moods data seeded.");
        }

        if (achievementRepository.count() == 0) {
            System.out.println("Seeding achievements data...");

            createAchievement("FIRST_ENTRY", "Bước chân đầu tiên", "Viết bài nhật ký đầu tiên của bạn.", "emoji_events");
            createAchievement("STREAK_7_DAYS", "Nhà văn kiên trì", "Duy trì chuỗi viết 7 ngày liên tiếp.", "local_fire_department");
            createAchievement("WRITE_10_ENTRIES", "Người ghi chép", "Viết đủ 10 bài nhật ký.", "history_edu");
            createAchievement("EXPLORE_ALL_MOODS", "Nhà thám hiểm cảm xúc", "Sử dụng tất cả 5 loại cảm xúc.", "mood");

            System.out.println("Achievements data seeded.");
        }
    }

    private void createAchievement(String key, String name, String description, String icon) {
        Achievement achievement = new Achievement();
        achievement.setAchievementKey(key);
        achievement.setName(name);
        achievement.setDescription(description);
        achievement.setBadgeIcon(icon);
        achievementRepository.save(achievement);
    }
}