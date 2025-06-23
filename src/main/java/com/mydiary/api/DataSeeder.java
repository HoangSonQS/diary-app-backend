package com.mydiary.api;

import com.mydiary.api.entity.Mood;
import com.mydiary.api.repository.MoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private MoodRepository moodRepository;

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
    }
}