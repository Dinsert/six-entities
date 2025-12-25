package com.example.six_entities.util;

import com.example.six_entities.model.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

public class UserFixture {

    public static final UUID ID = UUID.randomUUID();

    public static UserDto createUserDto() {
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setExternalBalance(new BigDecimal(1));
        userProfileDto.setLoyaltyLevel("SILVER");
        CouponDto couponDto = new CouponDto();
        couponDto.setId(UUID.randomUUID());
        couponDto.setDiscount(new BigDecimal(1));
        UserDto dto = new UserDto();
        dto.setId(UUID.randomUUID());
        dto.setUsername("username");
        dto.setUserProfileDto(userProfileDto);
        dto.setCoupons(Collections.singletonList(couponDto));
        return dto;
    }

    public static User createUserForSave() {
        Coupon coupon = new Coupon();
        coupon.setDiscount(new BigDecimal(1));
        User user = new User();
        user.setUsername("light");
        user.setCoupons(Collections.singletonList(coupon));
        return user;
    }

    public static UserProfileDto createProfileDto() {
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setLoyaltyLevel("GOLD");
        userProfileDto.setExternalBalance(new BigDecimal(1));
        return userProfileDto;
    }
}
