package com.example.userprofileservice.mapper;

import com.example.userprofile.api.dto.UserProfileDto;
import com.example.userprofileservice.model.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfileMapper {

    UserProfileDto toDto(UserProfile userProfile);

    @Mapping(target = "userId", ignore = true)
    UserProfile toEntity(UserProfileDto dto);

    @Mapping(target = "userId", ignore = true)
    void updateEntityFromDto(UserProfileDto dto, @MappingTarget UserProfile userProfile);
}
