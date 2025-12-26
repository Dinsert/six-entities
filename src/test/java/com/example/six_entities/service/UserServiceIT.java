package com.example.six_entities.service;

import com.example.six_entities.TestContainersConfiguration;
import com.example.six_entities.client.UserProfileClient;
import com.example.six_entities.exception.ObjectNotFoundException;
import com.example.six_entities.model.User;
import com.example.six_entities.model.UserDto;
import com.example.six_entities.model.UserProfileDto;
import com.example.six_entities.repository.UserRepository;
import com.example.six_entities.util.UserFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@SpringBootTest(classes = TestContainersConfiguration.class)
class UserServiceIT extends BaseIntegrationTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @MockBean
    UserProfileClient userProfileClient;

    static final String USERS_CACHE = "users";
    static final String PROFILES_CACHE = "user_profiles";

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        cacheManager.getCache(USERS_CACHE).clear();
        cacheManager.getCache(PROFILES_CACHE).clear();
    }

    @Test
    void createUser_shouldCacheResult() {
        UserDto dto = UserFixture.createUserDto();
        doNothing().when(userProfileClient).createProfile(dto.getId(), dto.getUserProfileDto());

        UserDto userDto = userService.createUser(dto);

        assertThat(cacheManager.getCache(USERS_CACHE).get(userDto.getId())).isNotNull();
        assertThat(cacheManager.getCache(USERS_CACHE).get(userDto.getId(), UserDto.class)).isEqualTo(userDto);
    }

    @Test
    void getUserById_shouldCacheResult() {
        User user = userRepository.save(UserFixture.createUserForSave());
        UserProfileDto userProfileDto = UserFixture.createProfileDto();
        when(userProfileClient.getProfile(user.getId())).thenReturn(userProfileDto);

        UserDto response = userService.getUserById(user.getId());

        assertThat(cacheManager.getCache(PROFILES_CACHE).get(user.getId())).isNotNull();
        assertThat(cacheManager.getCache(PROFILES_CACHE).get(user.getId(), UserProfileDto.class)).isEqualTo(userProfileDto);
        assertThat(cacheManager.getCache(USERS_CACHE).get(user.getId())).isNotNull();
        assertThat(cacheManager.getCache(USERS_CACHE).get(user.getId(), UserDto.class)).isEqualTo(response);
    }

    @Test
    void getUserById_shouldThrowObjectNotFoundException() {
        UUID notExistingId = UUID.randomUUID();

        assertThatThrownBy(() -> userService.getUserById(notExistingId))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("User not found")
                .hasMessageContaining(notExistingId.toString());

        verifyNoInteractions(userProfileClient);
    }

    @Test
    void updateUser_shouldUpdateCache() {
        UserDto dto = UserFixture.createUserDto();
        doNothing().when(userProfileClient).createProfile(dto.getId(), dto.getUserProfileDto());
        UserDto playerDto = userService.createUser(dto);
        playerDto.setUsername("change username");
        doNothing().when(userProfileClient).updateProfile(dto.getId(), dto.getUserProfileDto());

        userService.updateUser(playerDto);

        assertThat(cacheManager.getCache(USERS_CACHE).get(playerDto.getId(), UserDto.class)).isEqualTo(playerDto);
    }

    @Test
    void deleteUser_shouldEvictCache() {
        UserDto dto = UserFixture.createUserDto();
        doNothing().when(userProfileClient).createProfile(dto.getId(), dto.getUserProfileDto());
        UserDto playerDto = userService.createUser(dto);

        userService.deleteUser(playerDto.getId());

        assertThat(cacheManager.getCache(USERS_CACHE).get(playerDto.getId())).isNull();
        assertThat(userRepository.findById(playerDto.getId())).isEmpty();
    }
}