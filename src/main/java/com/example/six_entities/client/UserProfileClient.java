package com.example.six_entities.client;

import com.example.six_entities.model.UserProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(
        name = "user-profile",
        url = "${user-profile-service.url}",
        path = "/api/user-profiles/"
)
public interface UserProfileClient {

    @GetMapping("{userId}")
    UserProfileDto getProfile(@PathVariable UUID userId);

    @PostMapping("{userId}")
    void createProfile(
            @PathVariable UUID userId,
            @RequestBody UserProfileDto dto
    );

    @PutMapping("{userId}")
    void updateProfile(
            @PathVariable UUID userId,
            @RequestBody UserProfileDto dto
    );
}


