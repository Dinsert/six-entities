package com.example.six_entities.service.impl;

import com.example.six_entities.exception.FileNotFoundException;
import com.example.six_entities.exception.FileUploadException;
import com.example.six_entities.mapper.FileMapper;
import com.example.six_entities.model.File;
import com.example.six_entities.model.FileDto;
import com.example.six_entities.model.PresignedUrlDto;
import com.example.six_entities.repository.FileRepository;
import com.example.six_entities.service.FileService;
import com.example.six_entities.storage.S3Properties;
import com.example.six_entities.storage.S3Storage;
import com.example.six_entities.storage.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    private final S3Storage storage;
    private final S3Properties props;

    @Override
    public FileDto upload(MultipartFile file) {
        validateFile(file);

        String contentType = storage.normalizeContentType(file.getContentType());
        String s3Key = storage.newKey();
        String filename = S3Util.sanitizeFilename(file.getOriginalFilename());

        storage.putObject(s3Key, file, contentType);

        try {
            return fileMapper.toDto(fileRepository.save(fileMapper.toEntity(filename, s3Key, contentType, file.getSize())));
        } catch (RuntimeException e) {
            log.error("DB save failed after uploading file '{}' (s3Key={}), rolling back S3 object", filename, s3Key, e);
            storage.deleteQuietly(s3Key);
            throw e;
        }
    }

    @Override
    public PresignedUrlDto getDownloadUrl(Integer fileId) {
        File entity = fileRepository.findById(fileId).orElseThrow(() -> new FileNotFoundException(fileId));
        String url = storage.presignGetUrl(entity.getS3Key(), entity.getFilename());
        return fileMapper.toPresignedUrlDto(fileId, url, props.getPresignTtl().getSeconds());
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.warn("Upload attempt with empty file");
            throw new FileUploadException("File is empty");
        }
        if (file.getSize() > props.getMaxFileSize().toBytes()) {
            log.warn("Upload rejected: file too large ({} bytes, max {} bytes)", file.getSize(), props.getMaxFileSize().toBytes());
            throw new FileUploadException("File is too large. Max allowed size is " + props.getMaxFileSize().toMegabytes() + " MB");
        }
    }
}