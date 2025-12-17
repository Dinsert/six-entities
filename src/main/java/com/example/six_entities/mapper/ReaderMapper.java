package com.example.six_entities.mapper;

import com.example.six_entities.model.Reader;
import com.example.six_entities.model.ReaderDto;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = BookMapper.class)
public interface ReaderMapper {

    @Mapping(target = "books", source = "books")
    ReaderDto toDto(Reader reader);

    @Mapping(target = "id", ignore = true)
    Reader toEntity(ReaderDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    void updateEntityFromDto(ReaderDto dto, @MappingTarget Reader reader);

    @AfterMapping
    default void linkBooks(@MappingTarget Reader reader) {
        reader.getBooks().forEach(book -> book.setReader(reader));
    }
}

