package com.example.six_entities.service.impl;

import com.example.six_entities.client.UserProfileClient;
import com.example.six_entities.exception.ObjectNotFoundException;
import com.example.six_entities.mapper.CouponMapper;
import com.example.six_entities.mapper.UserMapper;
import com.example.six_entities.model.User;
import com.example.six_entities.model.UserDto;
import com.example.six_entities.model.UserProfileDto;
import com.example.six_entities.repository.UserRepository;
import com.example.six_entities.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CouponMapper couponMapper;
    private final UserProfileClient userProfileClient;

    @Transactional
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userRepository.save(userMapper.toEntity(userDto));
        userProfileClient.upsertProfile(user.getId(), userDto.getUserProfileDto());
        UserDto dto = userMapper.toDto(user);
        userMapper.updateDtoFromProfile(userDto.getUserProfileDto(), dto);
        return dto;
    }

    @Transactional
    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.warn("User not found: id={}", id);
            return new ObjectNotFoundException("User not found: id=" + id);
        });
        UserDto dto = userMapper.toDto(user);
        UserProfileDto profile = userProfileClient.getProfile(id);
        userMapper.updateDtoFromProfile(profile, dto);
        return dto;
    }

    @Transactional
    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow(() -> {
            log.warn("User not found: id={}", userDto.getId());
            return new ObjectNotFoundException("User not found: id=" + userDto.getId());
        });
        userMapper.updateEntityFromDto(userDto, user);
        userDto.getCoupons()
                .forEach(couponDto -> user.getCoupons().stream().filter(coupon -> coupon.getId().equals(couponDto.getId()))
                        .forEach(coupon -> couponMapper.updateEntityFromDto(couponDto, coupon)));
        userProfileClient.upsertProfile(user.getId(), userDto.getUserProfileDto());
        return userDto;
    }
}
