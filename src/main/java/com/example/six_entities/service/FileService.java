package com.example.six_entities.service;

import com.example.six_entities.model.FileDto;
import com.example.six_entities.model.PresignedUrlDto;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileDto upload(MultipartFile file);

    PresignedUrlDto getDownloadUrl(Integer fileId);
}
