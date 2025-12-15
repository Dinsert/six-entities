package com.example.six_entities.controller;

import com.example.six_entities.api.PlayerApi;
import com.example.six_entities.model.PlayerDto;
import com.example.six_entities.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class PlayerController implements PlayerApi {

    private final PlayerService playerService;

    @Override
    public ResponseEntity<PlayerDto> createPlayer(PlayerDto playerDto) {
        return ResponseEntity.ok(playerService.createPlayer(playerDto));
    }

    @Override
    public ResponseEntity<Void> deletePlayer(UUID id) {
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<PlayerDto> getPlayerById(UUID id) {
        return ResponseEntity.ok(playerService.getPlayerById(id));
    }

    @Override
    public ResponseEntity<PlayerDto> updatePlayer(PlayerDto playerDto) {
        return ResponseEntity.ok(playerService.updatePlayer(playerDto));
    }
}
