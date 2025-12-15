package com.example.six_entities.service;

import com.example.six_entities.model.ReaderDto;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public interface ReaderService {

    @Nullable
    ReaderDto createReader(ReaderDto readerDto);

    void deleteReader(UUID id);

    @Nullable
    ReaderDto getReaderById(UUID id);

    @Nullable
    ReaderDto updateReader(ReaderDto readerDto);
}
