package com.example.six_entities.controller;

import com.example.six_entities.api.ReaderApi;
import com.example.six_entities.model.ReaderDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ReaderController implements ReaderApi {

    @Override
    public ResponseEntity<ReaderDto> createReader(ReaderDto readerDto) {
        return ReaderApi.super.createReader(readerDto);
    }

    @Override
    public ResponseEntity<Void> deleteReader(UUID id) {
        return ReaderApi.super.deleteReader(id);
    }

    @Override
    public ResponseEntity<ReaderDto> getReaderById(UUID id) {
        return ReaderApi.super.getReaderById(id);
    }

    @Override
    public ResponseEntity<ReaderDto> updateReader(UUID id, ReaderDto readerDto) {
        return ReaderApi.super.updateReader(id, readerDto);
    }
}
