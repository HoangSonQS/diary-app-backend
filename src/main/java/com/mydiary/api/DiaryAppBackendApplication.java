package com.mydiary.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DiaryAppBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiaryAppBackendApplication.class, args);
    }

}
