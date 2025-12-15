package com.example.six_entities.service.impl;

import com.example.six_entities.mapper.PlayerMapper;
import com.example.six_entities.model.Player;
import com.example.six_entities.model.PlayerDto;
import com.example.six_entities.repository.PlayerRepository;
import com.example.six_entities.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
@RequiredArgsConstructor
@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    @Override
    public @Nullable PlayerDto createPlayer(PlayerDto playerDto) {
        Player player = playerMapper.toEntity(playerDto);
        return playerMapper.toDto(playerRepository.save(player));
    }

    @Override
    public void deletePlayer(UUID id) {
        playerRepository.deleteById(id);
    }

    @Override
    public @Nullable PlayerDto getPlayerById(UUID id) {
        return playerMapper.toDto(playerRepository.findById(id).orElseThrow());
    }

    @Override
    public @Nullable PlayerDto updatePlayer(PlayerDto playerDto) {
        Player player = playerRepository.findById(playerDto.getId()).orElseThrow();
        playerMapper.updateEntityFromDto(playerDto, player);
        return playerMapper.toDto(player);
    }
}
