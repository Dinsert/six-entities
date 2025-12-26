package com.example.six_entities.controller;

import com.example.six_entities.model.ReaderDto;
import com.example.six_entities.service.ReaderService;
import com.example.six_entities.util.ReaderFixture;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReaderController.class)
class ReaderControllerTest extends BaseWebMvcTest {

    @MockBean
    ReaderService readerService;

    final String uriPostOrPut = "/readers";
    final String uriGetOrDel = "/readers/{id}";

    @Test
    void createReader_shouldReturn200() throws Exception {
        ReaderDto dto = ReaderFixture.createReaderDto();
        String content = objectMapper.writeValueAsString(dto);

        when(readerService.createReader(dto)).thenReturn(dto);

        mockMvc.perform(post(uriPostOrPut)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(content().json(content));
    }


    @Test
    void createReader_invalidBody_shouldReturn400() throws Exception {
        mockMvc.perform(post(uriPostOrPut)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteReader_shouldReturn204() throws Exception {
        mockMvc.perform(delete(uriGetOrDel, ReaderFixture.ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void getReaderById_shouldReturn200() throws Exception {
        ReaderDto dto = ReaderFixture.createReaderDto();

        when(readerService.getReaderById(ReaderFixture.ID))
                .thenReturn(dto);

        mockMvc.perform(get(uriGetOrDel, ReaderFixture.ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dto)));
    }

    @Test
    void updateReader_shouldReturn200() throws Exception {
        ReaderDto dto = ReaderFixture.createReaderDto();
        String content = objectMapper.writeValueAsString(dto);

        when(readerService.updateReader(dto)).thenReturn(dto);

        mockMvc.perform(put(uriPostOrPut)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(content().json(content));
    }

    @Test
    void updateReader_invalidBody_shouldReturn400() throws Exception {
        mockMvc.perform(put(uriPostOrPut)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}