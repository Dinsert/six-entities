package com.example.six_entities.client;

import com.example.six_entities.model.UserProfileReq;
import com.example.six_entities.model.UserProfileRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        name = "user-profile",
        url = "http://localhost:8081"
)
public interface UserProfileClient {

    @GetMapping("/api/user-profiles/{userId}")
    UserProfileRes getProfile(@PathVariable UUID userId);

    @PutMapping("/api/user-profiles/{userId}")
    UserProfileRes upsertProfile(
            @PathVariable UUID userId,
            @RequestBody UserProfileReq dto
    );
}


