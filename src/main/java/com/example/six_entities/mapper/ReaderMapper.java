package com.example.six_entities.mapper;

import com.example.six_entities.model.Reader;
import com.example.six_entities.model.ReaderDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReaderMapper {

    @Mapping(target = "books", source = "books")
    ReaderDto toDto(Reader reader);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", source = "books")
    Reader toEntity(ReaderDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", source = "books")
    void updateEntityFromDto(ReaderDto dto, @MappingTarget Reader reader);

    @AfterMapping
    default void linkBooks(@MappingTarget Reader reader) {
        if (reader.getBooks() != null) {
            reader.getBooks().forEach(book -> book.setReader(reader));
        }
    }
}

