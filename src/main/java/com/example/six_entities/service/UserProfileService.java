package com.example.six_entities.service;

import com.example.six_entities.client.UserProfileClient;
import com.example.six_entities.model.UserProfileDto;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileClient userProfileClient;

    @Retry(name = "user-profile")
    public void upsertProfile(UUID userId, UserProfileDto dto) {
        log.info("Calling user-profile-service PUT for userId={}", userId);
        userProfileClient.upsertProfile(userId, dto);
    }

    @Cacheable(value = "user_profiles", key = "#userId")
    @Retry(name = "user-profile")
    public UserProfileDto getProfile(UUID userId) {
        log.info("Calling user-profile-service GET for userId={}", userId);
        return userProfileClient.getProfile(userId);
    }
}
