package com.example.six_entities.service;

import com.example.six_entities.model.ReaderDto;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public interface ReaderService {

    ReaderDto createReader(ReaderDto readerDto);

    void deleteReader(UUID id);

    ReaderDto getReaderById(UUID id);

    ReaderDto updateReader(ReaderDto readerDto);
}
