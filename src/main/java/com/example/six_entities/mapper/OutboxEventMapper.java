package com.example.six_entities.mapper;

import com.example.six_entities.exception.ConvertingException;
import com.example.six_entities.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.time.Instant;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = {Instant.class, OutboxEventStatus.class, UUID.class})
public interface OutboxEventMapper {

    @Mapping(target = "messageKey", expression = "java(event.getUserId().toString())")
    @Mapping(target = "payload", source = "event", qualifiedByName = "convertObjectToJson")
    @Mapping(target = "status", expression = "java(OutboxEventStatus.NEW)")
    @Mapping(target = "createdAt", expression = "java(Instant.now())")
    OutboxEvent toEntity(UserProfileEvent event);

    @Mapping(target = "eventId", expression = "java(UUID.randomUUID())")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "payload.loyaltyLevel", source = "userDto.userProfileDto.loyaltyLevel")
    @Mapping(target = "payload.externalBalance", source = "userDto.userProfileDto.externalBalance")
    UserProfileEvent toUserProfileEvent(User user, UserDto userDto, UserProfileEventType eventType);

    @Named("convertObjectToJson")
    default String convertObjectToJson(UserProfileEvent event) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new ConvertingException("Error converting UserProfileEvent to JSON", e);
        }
    }

}
