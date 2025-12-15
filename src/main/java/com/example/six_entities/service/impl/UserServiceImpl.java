package com.example.six_entities.service.impl;

import com.example.six_entities.mapper.UserMapper;
import com.example.six_entities.model.Coupon;
import com.example.six_entities.model.CouponDto;
import com.example.six_entities.model.User;
import com.example.six_entities.model.UserDto;
import com.example.six_entities.repository.CouponRepository;
import com.example.six_entities.repository.UserRepository;
import com.example.six_entities.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Transactional
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final UserMapper userMapper;

    @Override
    public @Nullable UserDto createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        List<CouponDto> couponsDto = userDto.getCoupons();
        List<Coupon> coupons = new ArrayList<>();
        for (CouponDto couponDto : couponsDto) {
            Coupon coupon = new Coupon();
            coupon.setDiscount(couponDto.getDiscount());
            coupons.add(couponRepository.save(coupon));
        }
        user.setCoupons(coupons);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public @Nullable UserDto getUserById(UUID id) {
        return userMapper.toDto(userRepository.findById(id).orElseThrow());
    }

    @Override
    public @Nullable UserDto updateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow();
        userMapper.updateEntityFromDto(userDto, user);
        List<Coupon> coupons = user.getCoupons();
        List<CouponDto> couponsDto = userDto.getCoupons();
        couponsDto.forEach(couponDto -> coupons.forEach(coupon -> {
            if (coupon.getId().equals(couponDto.getId())) {
                coupon.setDiscount(couponDto.getDiscount());
            }
        }));
        return userMapper.toDto(user);
    }
}
