package com.example.six_entities.service;

import com.example.six_entities.model.PlayerDto;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public interface PlayerService {

    @Nullable
    PlayerDto createPlayer(PlayerDto playerDto);

    void deletePlayer(UUID id);

    @Nullable
    PlayerDto getPlayerById(UUID id);

    @Nullable
    PlayerDto updatePlayer(PlayerDto playerDto);
}
