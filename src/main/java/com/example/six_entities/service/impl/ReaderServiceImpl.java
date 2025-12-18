package com.example.six_entities.service.impl;

import com.example.six_entities.exception.ObjectNotFoundException;
import com.example.six_entities.mapper.BookMapper;
import com.example.six_entities.mapper.ReaderMapper;
import com.example.six_entities.model.Reader;
import com.example.six_entities.model.ReaderDto;
import com.example.six_entities.repository.ReaderRepository;
import com.example.six_entities.service.ReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;
    private final ReaderMapper readerMapper;
    private final BookMapper bookMapper;

    @Transactional
    @Override
    public ReaderDto createReader(ReaderDto readerDto) {
        Reader reader = readerRepository.save(readerMapper.toEntity(readerDto));
        return readerMapper.toDto(reader);
    }

    @Transactional
    @Override
    public void deleteReader(UUID id) {
        readerRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public ReaderDto getReaderById(UUID id) {
        return readerMapper.toDto(readerRepository.findById(id).orElseThrow(() -> {
            log.warn("Reader not found: id={}", id);
            return new ObjectNotFoundException("Reader not found: id=" + id);
        }));
    }

    @Transactional
    @Override
    public ReaderDto updateReader(ReaderDto readerDto) {
        Reader reader = readerRepository.findById(readerDto.getId()).orElseThrow(() -> {
            log.warn("Reader not found: id={}", readerDto.getId());
            return new ObjectNotFoundException("Reader not found: id=" + readerDto.getId());
        });
        readerMapper.updateEntityFromDto(readerDto, reader);
        readerDto.getBooks()
                .forEach(bookDto -> reader.getBooks().stream().filter(book -> book.getId().equals(bookDto.getId()))
                        .forEach(book -> bookMapper.updateEntityFromDto(bookDto, book)));
        return readerMapper.toDto(reader);
    }
}
