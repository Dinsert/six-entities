package com.example.six_entities.controller;

import com.example.six_entities.api.UserApi;
import com.example.six_entities.model.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class UserController implements UserApi {


    @Override
    public ResponseEntity<UserDto> createUser(UserDto userDto) {
        return UserApi.super.createUser(userDto);
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID id) {
        return UserApi.super.deleteUser(id);
    }

    @Override
    public ResponseEntity<UserDto> getUserById(UUID id) {
        return UserApi.super.getUserById(id);
    }

    @Override
    public ResponseEntity<UserDto> updateUser(UUID id, UserDto userDto) {
        return UserApi.super.updateUser(id, userDto);
    }
}
