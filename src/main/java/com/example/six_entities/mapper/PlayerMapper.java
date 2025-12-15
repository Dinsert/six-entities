package com.example.six_entities.mapper;

import com.example.six_entities.model.Player;
import com.example.six_entities.model.PlayerDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    PlayerDto toDto(Player player);

    Player toEntity(PlayerDto dto);
}
