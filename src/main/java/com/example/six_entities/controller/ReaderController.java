package com.example.six_entities.controller;

import com.example.six_entities.api.ReaderApi;
import com.example.six_entities.model.ReaderDto;
import com.example.six_entities.service.ReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class ReaderController implements ReaderApi {

    private final ReaderService readerService;


    @Override
    public ResponseEntity<ReaderDto> createReader(ReaderDto readerDto) {
        return ResponseEntity.ok(readerService.createReader(readerDto));
    }

    @Override
    public ResponseEntity<Void> deleteReader(UUID id) {
        readerService.deleteReader(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ReaderDto> getReaderById(UUID id) {
        return ResponseEntity.ok(readerService.getReaderById(id));
    }

    @Override
    public ResponseEntity<ReaderDto> updateReader(ReaderDto readerDto) {
        return ResponseEntity.ok(readerService.updateReader(readerDto));
    }
}
