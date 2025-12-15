package com.example.six_entities.mapper;

import com.example.six_entities.model.Player;
import com.example.six_entities.model.PlayerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CentralMapperConfig.class)
public interface PlayerMapper {

    PlayerDto toDto(Player player);

    @Mapping(target = "id", ignore = true)
    Player toEntity(PlayerDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(PlayerDto dto, @MappingTarget Player player);

}

