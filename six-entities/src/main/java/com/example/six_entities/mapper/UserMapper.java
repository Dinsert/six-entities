package com.example.six_entities.mapper;

import com.example.six_entities.model.User;
import com.example.six_entities.model.UserDto;
import com.example.six_entities.model.UserProfileDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = CouponMapper.class)
public interface UserMapper {

    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "coupons", ignore = true)
    void updateEntityFromDto(UserDto dto, @MappingTarget User user);

    @Mapping(target = "userProfileDto.loyaltyLevel", source = "loyaltyLevel")
    @Mapping(target = "userProfileDto.externalBalance", source = "externalBalance")
    void updateDtoFromProfile(UserProfileDto profile, @MappingTarget UserDto dto);
}

