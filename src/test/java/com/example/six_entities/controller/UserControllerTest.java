package com.example.six_entities.controller;

import com.example.six_entities.config.BaseWebMvcTest;
import com.example.six_entities.model.UserDto;
import com.example.six_entities.service.UserService;
import com.example.six_entities.util.UserFixture;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest extends BaseWebMvcTest {

    @MockBean
    UserService userService;

    final String uriPostOrPut = "/users";
    final String uriGetOrDel = "/users/{id}";

    @Test
    void createUser_shouldReturn200() throws Exception {
        UserDto dto = UserFixture.createUserDto();
        String content = objectMapper.writeValueAsString(dto);

        when(userService.createUser(dto)).thenReturn(dto);

        mockMvc.perform(post(uriPostOrPut)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(content().json(content));
    }


    @Test
    void createUser_invalidBody_shouldReturn400() throws Exception {
        mockMvc.perform(post(uriPostOrPut)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser_shouldReturn204() throws Exception {
        mockMvc.perform(delete(uriGetOrDel, UserFixture.ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void getUserById_shouldReturn200() throws Exception {
        UserDto dto = UserFixture.createUserDto();

        when(userService.getUserById(UserFixture.ID))
                .thenReturn(dto);

        mockMvc.perform(get(uriGetOrDel, UserFixture.ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dto)));
    }

    @Test
    void updateUser_shouldReturn200() throws Exception {
        UserDto dto = UserFixture.createUserDto();
        String content = objectMapper.writeValueAsString(dto);

        when(userService.updateUser(dto)).thenReturn(dto);

        mockMvc.perform(put(uriPostOrPut)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(content().json(content));
    }

    @Test
    void updateUser_invalidBody_shouldReturn400() throws Exception {
        mockMvc.perform(put(uriPostOrPut)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}