package com.example.six_entities.mapper;

import com.example.six_entities.model.File;
import com.example.six_entities.model.FileDto;
import com.example.six_entities.model.PresignedUrlDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.time.Instant;
import java.time.ZoneOffset;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = {Instant.class, ZoneOffset.class})
public interface FileMapper {

    @Mapping(target = "createdAt", expression = "java(file.getCreatedAt().atOffset(ZoneOffset.UTC))")
    FileDto toDto(File file);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(Instant.now())")
    File toEntity(String filename, String s3Key, String contentType, long size);

    PresignedUrlDto toPresignedUrlDto(Integer id, String url, Long expiresInSeconds);
}
