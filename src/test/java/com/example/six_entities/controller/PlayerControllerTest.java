package com.example.six_entities.controller;

import com.example.six_entities.exception.ObjectNotFoundException;
import com.example.six_entities.model.PlayerDto;
import com.example.six_entities.service.PlayerService;
import com.example.six_entities.util.PlayerFixture;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlayerController.class)
class PlayerControllerTest extends BaseWebMvcTest {

    @MockBean
    PlayerService playerService;

    final String uriPostOrPut = "/players";
    final String uriGetOrDel = "/players/{id}";

    @Test
    void createPlayer_shouldReturn200() throws Exception {
        PlayerDto dto = PlayerFixture.createPlayerDto();
        String content = objectMapper.writeValueAsString(dto);

        when(playerService.createPlayer(dto)).thenReturn(dto);

        mockMvc.perform(post(uriPostOrPut)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(content().json(content));
    }


    @Test
    void createPlayer_invalidBody_shouldReturn400() throws Exception {
        mockMvc.perform(post(uriPostOrPut)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletePlayer_shouldReturn204() throws Exception {
        mockMvc.perform(delete(uriGetOrDel, PlayerFixture.ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void getPlayerById_shouldReturn200() throws Exception {
        PlayerDto dto = PlayerFixture.createPlayerDto();

        when(playerService.getPlayerById(PlayerFixture.ID))
                .thenReturn(dto);

        mockMvc.perform(get(uriGetOrDel, PlayerFixture.ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dto)));
    }

    @Test
    void getPlayerById_shouldReturn404() throws Exception {
        when(playerService.getPlayerById(PlayerFixture.ID))
                .thenThrow(new ObjectNotFoundException("Player not found"));

        mockMvc.perform(get(uriGetOrDel, PlayerFixture.ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void updatePlayer_shouldReturn200() throws Exception {
        PlayerDto dto = PlayerFixture.createPlayerDto();
        String content = objectMapper.writeValueAsString(dto);

        when(playerService.updatePlayer(dto)).thenReturn(dto);

        mockMvc.perform(put(uriPostOrPut)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(content().json(content));
    }

    @Test
    void updatePlayer_invalidBody_shouldReturn400() throws Exception {
        mockMvc.perform(put(uriPostOrPut)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}