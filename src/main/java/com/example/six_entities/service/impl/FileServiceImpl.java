package com.example.six_entities.service.impl;

import com.example.six_entities.exception.FileNotFoundException;
import com.example.six_entities.exception.FileUploadException;
import com.example.six_entities.exception.StorageException;
import com.example.six_entities.mapper.FileMapper;
import com.example.six_entities.model.File;
import com.example.six_entities.model.FileDto;
import com.example.six_entities.model.PresignedUrlDto;
import com.example.six_entities.repository.FileRepository;
import com.example.six_entities.service.FileService;
import com.example.six_entities.storage.S3Properties;
import com.example.six_entities.storage.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    private final S3Client s3;
    private final S3Presigner presigner;
    private final S3Properties props;

    @Override
    @Transactional
    public FileDto upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileUploadException("File is empty");
        }

        String filename = Objects.requireNonNullElse(file.getOriginalFilename(), "file");
        String contentType = file.getContentType();
        long size = file.getSize();

        String s3Key = UUID.randomUUID() + "_" + filename;

        try {
            PutObjectRequest putReq = PutObjectRequest.builder()
                    .bucket(props.getBucket())
                    .key(s3Key)
                    .contentType(contentType)
                    .contentLength(size)
                    .build();

            s3.putObject(putReq, RequestBody.fromInputStream(file.getInputStream(), size));
        } catch (IOException e) {
            throw new FileUploadException("Failed to read upload stream", e);
        } catch (S3Exception e) {
            throw new StorageException("Failed to upload to S3: " + S3Util.awsMessage(e), e);
        }
        return fileMapper.toDto(fileRepository.save(fileMapper.toEntity(filename, s3Key, contentType, size)));
    }

    @Override
    @Transactional(readOnly = true)
    public PresignedUrlDto getDownloadUrl(Long fileId) {
        File entity = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException(fileId));

        Duration ttl = props.getPresignTtl();

        GetObjectRequest getReq = GetObjectRequest.builder()
                .bucket(props.getBucket())
                .key(entity.getS3Key())
                .responseContentDisposition("attachment; filename=\"" + S3Util.sanitizeFilename(entity.getFilename()) + "\"")
                .build();

        try {
            PresignedGetObjectRequest presigned = presigner.presignGetObject(
                    GetObjectPresignRequest.builder()
                            .signatureDuration(ttl)
                            .getObjectRequest(getReq)
                            .build()
            );
            return fileMapper.toPresignedUrlDto(fileId, presigned.url().toString(), ttl.getSeconds());
        } catch (S3Exception e) {
            throw new StorageException("Failed to generate presigned URL: " + S3Util.awsMessage(e), e);
        }
    }
}