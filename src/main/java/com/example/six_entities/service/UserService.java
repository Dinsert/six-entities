package com.example.six_entities.service;

import com.example.six_entities.model.UserDto;

import java.util.UUID;

public interface UserService {

    UserDto createUser(UserDto userDto);

    void deleteUser(UUID id);

    UserDto getUserById(UUID id);

    UserDto updateUser(UserDto userDto);
}