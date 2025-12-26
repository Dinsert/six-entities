package com.example.six_entities.service;

import com.example.six_entities.TestContainersConfiguration;
import com.example.six_entities.exception.ObjectNotFoundException;
import com.example.six_entities.model.Player;
import com.example.six_entities.model.PlayerDto;
import com.example.six_entities.repository.PlayerRepository;
import com.example.six_entities.util.PlayerFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = TestContainersConfiguration.class)
class PlayerServiceIT extends BaseIntegrationTest {

    @Autowired
    PlayerService playerService;

    @Autowired
    PlayerRepository playerRepository;

    static final String CACHE = "players";

    @AfterEach
    void tearDown() {
        playerRepository.deleteAll();
        cacheManager.getCache(CACHE).clear();
    }

    @Test
    void createPlayer_shouldCacheResult() {
        PlayerDto dto = PlayerFixture.createPlayerDto();

        PlayerDto player = playerService.createPlayer(dto);

        assertThat(cacheManager.getCache(CACHE).get(player.getId())).isNotNull();
        assertThat(cacheManager.getCache(CACHE).get(player.getId(), PlayerDto.class)).isEqualTo(player);
    }

    @Test
    void getPlayerById_shouldCacheResult() {
        Player player = playerRepository.save(PlayerFixture.createPlayerForSave());

        PlayerDto response = playerService.getPlayerById(player.getId());

        assertThat(cacheManager.getCache(CACHE).get(player.getId())).isNotNull();
        assertThat(cacheManager.getCache(CACHE).get(player.getId(), PlayerDto.class)).isEqualTo(response);
    }

    @Test
    void getPlayerById_shouldThrowObjectNotFoundException() {
        UUID notExistingId = UUID.randomUUID();

        assertThatThrownBy(() -> playerService.getPlayerById(notExistingId))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Player not found")
                .hasMessageContaining(notExistingId.toString());
    }

    @Test
    void updatePlayer_shouldUpdateCache() {
        PlayerDto dto = PlayerFixture.createPlayerDto();
        PlayerDto playerDto = playerService.createPlayer(dto);
        playerDto.setName("change name");

        playerService.updatePlayer(playerDto);

        assertThat(cacheManager.getCache(CACHE).get(playerDto.getId(), PlayerDto.class)).isEqualTo(playerDto);
    }

    @Test
    void deletePlayer_shouldEvictCache() {
        PlayerDto dto = PlayerFixture.createPlayerDto();
        PlayerDto playerDto = playerService.createPlayer(dto);

        playerService.deletePlayer(playerDto.getId());

        assertThat(cacheManager.getCache(CACHE).get(playerDto.getId())).isNull();
        assertThat(playerRepository.findById(playerDto.getId())).isEmpty();
    }

}