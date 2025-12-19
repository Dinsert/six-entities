package com.example.six_entities.client;

import com.example.six_entities.config.UserProfileClientConfig;
import com.example.six_entities.model.UserProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        name = "user-profile",
        url = "${user-profile-service.url}",
        path = "/api/user-profiles/",
        configuration = UserProfileClientConfig.class
)
public interface UserProfileClient {

    @GetMapping("{userId}")
    UserProfileDto getProfile(@PathVariable UUID userId);

    @PutMapping("{userId}")
    void upsertProfile(
            @PathVariable UUID userId,
            @RequestBody UserProfileDto dto
    );
}


