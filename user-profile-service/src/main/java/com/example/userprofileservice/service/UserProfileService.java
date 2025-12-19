package com.example.userprofileservice.service;

import com.example.userprofile.api.dto.UserProfileDto;

import java.util.UUID;

public interface UserProfileService {

    UserProfileDto getUserProfile(UUID userId);

    void upsertUserProfile(UUID userId, UserProfileDto userProfileDto);
}
