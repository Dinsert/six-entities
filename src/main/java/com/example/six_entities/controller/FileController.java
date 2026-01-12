package com.example.six_entities.controller;

import com.example.six_entities.api.FileApi;
import com.example.six_entities.model.FileDto;
import com.example.six_entities.model.PresignedUrlDto;
import com.example.six_entities.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class FileController implements FileApi {

    private final FileService fileService;

    @Override
    public ResponseEntity<FileDto> uploadFile(MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fileService.upload(file));
    }

    @Override
    public ResponseEntity<PresignedUrlDto> getFileDownloadUrl(Integer id) {
        return ResponseEntity.ok(fileService.getDownloadUrl(id));
    }
}
