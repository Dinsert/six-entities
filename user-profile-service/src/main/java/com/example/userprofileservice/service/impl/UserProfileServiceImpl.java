package com.example.userprofileservice.service.impl;

import com.example.userprofile.api.dto.UserProfileDto;
import com.example.userprofileservice.exception.UserProfileNotFoundException;
import com.example.userprofileservice.mapper.UserProfileMapper;
import com.example.userprofileservice.model.UserProfile;
import com.example.userprofileservice.repository.UserProfileRepository;
import com.example.userprofileservice.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;


    @Transactional(readOnly = true)
    @Override
    public UserProfileDto getUserProfile(UUID userId) {
        UserProfile userProfile = userProfileRepository.findById(userId).orElseThrow(() -> {
            log.warn("User profile not found:{}", userId);
            return new UserProfileNotFoundException("User profile not found:" + userId);
        });
        return userProfileMapper.toDto(userProfile);
    }

    @Transactional
    @Override
    public void upsertUserProfile(UUID userId, UserProfileDto dto) {
        UserProfile profile = userProfileMapper.toEntity(dto);
        profile.setUserId(userId);
        userProfileRepository.save(profile);
    }
}
