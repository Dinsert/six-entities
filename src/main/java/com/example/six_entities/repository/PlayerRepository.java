package com.example.six_entities.repository;

import com.example.six_entities.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface PlayerRepository extends JpaRepository<Player, UUID> {
}
