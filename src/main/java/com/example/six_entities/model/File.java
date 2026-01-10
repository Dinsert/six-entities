package com.example.six_entities.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "files", schema = "app")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String filename;

    @Column(name = "s3_key", nullable = false, unique = true)
    private String s3Key;

    private String contentType;

    private Long size;

    private Instant createdAt = Instant.now();
}
