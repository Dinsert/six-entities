package com.example.six_entities.util;

import com.example.six_entities.model.Book;
import com.example.six_entities.model.BookDto;
import com.example.six_entities.model.Reader;
import com.example.six_entities.model.ReaderDto;

import java.util.Collections;
import java.util.UUID;

public class ReaderFixture {

    public static final UUID ID = UUID.randomUUID();

    public static ReaderDto createReaderDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(UUID.randomUUID());
        bookDto.setName("name");
        bookDto.setAuthor("author");
        ReaderDto dto = new ReaderDto();
        dto.setId(UUID.randomUUID());
        dto.setFirstName("first name");
        dto.setLastName("last name");
        dto.setBooks(Collections.singletonList(bookDto));
        return dto;
    }

    public static Reader createReaderForSave() {
        Book book = new Book();
        book.setName("game");
        book.setAuthor("rax");
        Reader reader = new Reader();
        book.setReader(reader);
        reader.setFirstName("Paul");
        reader.setLastName("Potter");
        reader.setBooks(Collections.singletonList(book));
        return reader;
    }
}
