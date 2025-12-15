package com.example.six_entities.service.impl;

import com.example.six_entities.exception.ReaderNotFoundException;
import com.example.six_entities.mapper.ReaderMapper;
import com.example.six_entities.model.Book;
import com.example.six_entities.model.BookDto;
import com.example.six_entities.model.Reader;
import com.example.six_entities.model.ReaderDto;
import com.example.six_entities.repository.BookRepository;
import com.example.six_entities.repository.ReaderRepository;
import com.example.six_entities.service.ReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;
    private final ReaderMapper readerMapper;
    private final BookRepository bookRepository;

    @Transactional
    @Override
    public ReaderDto createReader(ReaderDto readerDto) {
        Reader reader = readerRepository.save(readerMapper.toEntity(readerDto));
        List<BookDto> booksDto = readerDto.getBooks();
        List<Book> books = new ArrayList<>();
        for (BookDto bookDto : booksDto) {
            Book book = new Book();
            book.setName(bookDto.getName());
            book.setAuthor(bookDto.getAuthor());
            book.setReader(reader);
            books.add(bookRepository.save(book));
        }
        reader.setBooks(books);
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
            return new ReaderNotFoundException();
        }));
    }

    @Transactional
    @Override
    public ReaderDto updateReader(ReaderDto readerDto) {
        Reader reader = readerRepository.findById(readerDto.getId()).orElseThrow(() -> {
            log.warn("Reader not found: id={}", readerDto.getId());
            return new ReaderNotFoundException();
        });
        readerMapper.updateEntityFromDto(readerDto, reader);
        return readerMapper.toDto(readerRepository.save(reader));
    }
}
