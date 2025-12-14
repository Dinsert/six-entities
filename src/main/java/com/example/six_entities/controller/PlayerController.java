package com.example.six_entities.controller;

import com.example.six_entities.api.PlayerApi;
import com.example.six_entities.model.PlayerDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class PlayerController implements PlayerApi {

    @Override
    public ResponseEntity<PlayerDto> createPlayer(PlayerDto playerDto) {
        return PlayerApi.super.createPlayer(playerDto);
    }

    @Override
    public ResponseEntity<Void> deletePlayer(UUID id) {
        return PlayerApi.super.deletePlayer(id);
    }

    @Override
    public ResponseEntity<PlayerDto> getPlayerById(UUID id) {
        return PlayerApi.super.getPlayerById(id);
    }

    @Override
    public ResponseEntity<PlayerDto> updatePlayer(UUID id, PlayerDto playerDto) {
        return PlayerApi.super.updatePlayer(id, playerDto);
    }
}
