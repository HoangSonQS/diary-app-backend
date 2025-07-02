package com.mydiary.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync // Bật tính năng xử lý bất đồng bộ của Spring
public class AsyncConfig {
}