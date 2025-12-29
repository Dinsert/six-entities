package com.example.six_entities.mapper;

import com.example.six_entities.model.OutboxEvent;
import com.example.six_entities.model.User;
import com.example.six_entities.model.UserDto;
import com.example.six_entities.model.UserProfileCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.time.Instant;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = Instant.class)
public interface OutboxEventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "aggregateType", constant = "USER")
    @Mapping(target = "aggregateId", source = "user.id")
    @Mapping(target = "eventType", constant = "USER_PROFILE_CREATED")
    @Mapping(target = "payload", source = "event", qualifiedByName = "convertObjectToJson")
    @Mapping(target = "status", constant = "NEW")
    @Mapping(target = "createdAt", expression = "java(Instant.now())")
    @Mapping(target = "processedAt", ignore = true)
    OutboxEvent toEntity(User user, UserProfileCreatedEvent event);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "loyaltyLevel", source = "userDto.userProfileDto.loyaltyLevel")
    @Mapping(target = "externalBalance", source = "userDto.userProfileDto.externalBalance")
    UserProfileCreatedEvent toUserProfileCreatedEvent(User user, UserDto userDto);

    @Named("convertObjectToJson")
    default String convertObjectToJson(UserProfileCreatedEvent event) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting UserProfileCreatedEvent to JSON", e);
        }
    }

}
