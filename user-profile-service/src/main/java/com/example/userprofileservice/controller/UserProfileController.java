package com.example.userprofileservice.controller;

import com.example.userprofile.api.UserProfileApi;
import com.example.userprofile.api.dto.UserProfileDto;
import com.example.userprofileservice.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class UserProfileController implements UserProfileApi {

    private final UserProfileService userProfileService;

    @Override
    public ResponseEntity<UserProfileDto> getUserProfile(UUID userId) {
        return ResponseEntity.ok(userProfileService.getUserProfile(userId));
    }

    @Override
    public ResponseEntity<Void> upsertUserProfile(UUID userId, UserProfileDto userProfileDto) {
        userProfileService.upsertUserProfile(userId, userProfileDto);
        return ResponseEntity.ok().build();
    }
}
