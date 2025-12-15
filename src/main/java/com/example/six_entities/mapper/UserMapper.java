package com.example.six_entities.mapper;

import com.example.six_entities.model.User;
import com.example.six_entities.model.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CentralMapperConfig.class)
public interface UserMapper {

    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "coupons", ignore = true)
    User toEntity(UserDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "coupons", ignore = true)
    void updateEntityFromDto(UserDto dto, @MappingTarget User user);
}

