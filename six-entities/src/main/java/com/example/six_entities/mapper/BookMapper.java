package com.example.six_entities.mapper;

import com.example.six_entities.model.Book;
import com.example.six_entities.model.BookDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {

    BookDto toDto(Book book);

    @Mapping(target = "id", ignore = true)
    Book toEntity(BookDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(BookDto dto, @MappingTarget Book book);

}
