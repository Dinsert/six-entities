package com.example.six_entities.service.impl;

import com.example.six_entities.exception.ObjectNotFoundException;
import com.example.six_entities.mapper.PlayerMapper;
import com.example.six_entities.model.Player;
import com.example.six_entities.model.PlayerDto;
import com.example.six_entities.repository.PlayerRepository;
import com.example.six_entities.service.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    @Transactional
    @Override
    public PlayerDto createPlayer(PlayerDto playerDto) {
        Player player = playerMapper.toEntity(playerDto);
        return playerMapper.toDto(playerRepository.save(player));
    }

    @Transactional
    @Override
    public void deletePlayer(UUID id) {
        playerRepository.deleteById(id);
    }

    @Cacheable(
            value = "players",
            key = "#id"
    )
    @Transactional(readOnly = true)
    @Override
    public PlayerDto getPlayerById(UUID id) {
        log.info("DB call for player {}", id);
        return playerMapper.toDto(playerRepository.findById(id).orElseThrow(() -> {
            log.warn("Player not found: id={}", id);
            return new ObjectNotFoundException("Player not found: id=" + id);
        }));
    }

    @Transactional
    @Override
    public PlayerDto updatePlayer(PlayerDto playerDto) {
        Player player = playerRepository.findById(playerDto.getId()).orElseThrow(() -> {
            log.warn("Player not found: id={}", playerDto.getId());
            return new ObjectNotFoundException("Player not found: id=" + playerDto.getId());
        });
        playerMapper.updateEntityFromDto(playerDto, player);
        return playerMapper.toDto(player);
    }
}
