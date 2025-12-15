package com.example.six_entities.service.impl;

import com.example.six_entities.mapper.ReaderMapper;
import com.example.six_entities.model.Reader;
import com.example.six_entities.model.ReaderDto;
import com.example.six_entities.repository.ReaderRepository;
import com.example.six_entities.service.ReaderService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
@RequiredArgsConstructor
@Service
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;
    private final ReaderMapper readerMapper;

    @Override
    public @Nullable ReaderDto createReader(ReaderDto readerDto) {
        Reader reader = readerMapper.toEntity(readerDto);
        return readerMapper.toDto(readerRepository.save(reader));
    }

    @Override
    public void deleteReader(UUID id) {
        readerRepository.deleteById(id);
    }

    @Override
    public @Nullable ReaderDto getReaderById(UUID id) {
        return readerMapper.toDto(readerRepository.findById(id).orElseThrow());
    }

    @Override
    public @Nullable ReaderDto updateReader(ReaderDto readerDto) {
        Reader reader = readerRepository.findById(readerDto.getId()).orElseThrow();
        readerMapper.updateEntityFromDto(readerDto,reader);
        return readerMapper.toDto(reader);
    }
}
