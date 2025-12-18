package com.example.userprofileservice.controller;

import com.example.userprofile.api.UserProfileApi;
import com.example.userprofile.api.dto.UserProfileDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class UserProfileController implements UserProfileApi {

    @Override
    public ResponseEntity<UserProfileDto> getUserProfile(UUID userId) {
        return UserProfileApi.super.getUserProfile(userId);
    }

    @Override
    public ResponseEntity<UserProfileDto> putUserProfile(UserProfileDto userProfileDto) {
        return UserProfileApi.super.putUserProfile(userProfileDto);
    }
}
