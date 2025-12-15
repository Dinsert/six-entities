package com.example.six_entities.service;

import com.example.six_entities.model.PlayerDto;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public interface PlayerService {

    PlayerDto createPlayer(PlayerDto playerDto);

    void deletePlayer(UUID id);

    PlayerDto getPlayerById(UUID id);

    PlayerDto updatePlayer(PlayerDto playerDto);
}
