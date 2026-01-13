package com.example.six_entities.storage;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface S3Storage {

    void putObject(String key, MultipartFile file, String contentType);

    void deleteQuietly(String key);

    String presignGetUrl(String key, String downloadFilename);

    default String normalizeContentType(String contentType) {
        return (contentType == null || contentType.isBlank())
                ? "application/octet-stream"
                : contentType;
    }

    default String newKey() {
        return UUID.randomUUID().toString();
    }
}
