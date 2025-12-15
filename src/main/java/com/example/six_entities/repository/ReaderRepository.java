package com.example.six_entities.repository;

import com.example.six_entities.model.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, UUID> {
}
