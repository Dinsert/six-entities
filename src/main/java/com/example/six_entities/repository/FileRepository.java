package com.example.six_entities.repository;

import com.example.six_entities.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
