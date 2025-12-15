package com.example.six_entities.service.impl;

import com.example.six_entities.mapper.ReaderMapper;
import com.example.six_entities.model.Book;
import com.example.six_entities.model.BookDto;
import com.example.six_entities.model.Reader;
import com.example.six_entities.model.ReaderDto;
import com.example.six_entities.repository.BookRepository;
import com.example.six_entities.repository.ReaderRepository;
import com.example.six_entities.service.ReaderService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Transactional
@RequiredArgsConstructor
@Service
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;
    private final ReaderMapper readerMapper;
    private final BookRepository bookRepository;

    @Override
    public @Nullable ReaderDto createReader(ReaderDto readerDto) {
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
        readerMapper.updateEntityFromDto(readerDto, reader);
        return readerMapper.toDto(readerRepository.save(reader));
    }
}
