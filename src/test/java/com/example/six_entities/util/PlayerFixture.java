package com.example.six_entities.util;

import com.example.six_entities.model.Player;
import com.example.six_entities.model.PlayerDto;
import com.example.six_entities.model.Profile;
import com.example.six_entities.model.ProfileDto;

import java.util.UUID;

public class PlayerFixture {

    public static final UUID ID = UUID.randomUUID();

    public static PlayerDto createPlayerDto() {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setId(UUID.randomUUID());
        profileDto.setLogin("user");
        profileDto.setPassword("password");
        PlayerDto dto = new PlayerDto();
        dto.setId(UUID.randomUUID());
        dto.setName("Ivan");
        dto.setProfile(profileDto);
        return dto;
    }

    public static Player createPlayerForSave() {
        Profile profile = new Profile();
        profile.setLogin("login");
        profile.setPassword("123");
        Player player = new Player();
        player.setName("Paul");
        player.setProfile(profile);
        return player;
    }
}
