package com.example.six_entities.service;

import com.example.six_entities.model.UserDto;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public interface UserService {

    @Nullable
    UserDto createUser(UserDto userDto);


    void deleteUser(UUID id);

    @Nullable
    UserDto getUserById(UUID id);

    @Nullable
    UserDto updateUser(UserDto userDto);
}
