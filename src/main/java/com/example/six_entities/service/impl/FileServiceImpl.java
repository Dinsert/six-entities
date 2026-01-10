package com.example.six_entities.service.impl;

import com.example.six_entities.exception.FileNotFoundException;
import com.example.six_entities.exception.FileUploadException;
import com.example.six_entities.exception.StorageException;
import com.example.six_entities.model.File;
import com.example.six_entities.model.FileDto;
import com.example.six_entities.model.PresignedUrlDto;
import com.example.six_entities.repository.FileRepository;
import com.example.six_entities.service.FileService;
import com.example.six_entities.storage.S3Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final S3Client s3;
    private final S3Presigner presigner;
    private final S3Properties props;

    @Override
    @Transactional
    public FileDto upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileUploadException("File is empty");
        }

        ensureBucketExists(props.getBucket());

        String originalName = Objects.requireNonNullElse(file.getOriginalFilename(), "file");
        String contentType = file.getContentType();
        long size = file.getSize();

        String key = UUID.randomUUID() + "_" + originalName;

        try {
            PutObjectRequest putReq = PutObjectRequest.builder()
                    .bucket(props.getBucket())
                    .key(key)
                    .contentType(contentType)
                    .contentLength(size)
                    .build();

            s3.putObject(putReq, RequestBody.fromInputStream(file.getInputStream(), size));
        } catch (IOException e) {
            throw new FileUploadException("Failed to read upload stream", e);
        } catch (S3Exception e) {
            throw new StorageException("Failed to upload to S3: " + awsMessage(e), e);
        }

        File entity = new File();
        entity.setFilename(originalName);
        entity.setS3Key(key);
        entity.setContentType(contentType);
        entity.setSize(size);
        entity.setCreatedAt(Instant.now());

        File saved = fileRepository.save(entity);

        return toFileDto(saved);
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
                .responseContentDisposition("attachment; filename=\"" + sanitizeFilename(entity.getFilename()) + "\"")
                .build();

        try {
            PresignedGetObjectRequest presigned = presigner.presignGetObject(
                    GetObjectPresignRequest.builder()
                            .signatureDuration(ttl)
                            .getObjectRequest(getReq)
                            .build()
            );

            PresignedUrlDto dto = new PresignedUrlDto();
            dto.setId(fileId);
            dto.setUrl(presigned.url().toURI());
            dto.setExpiresInSeconds(ttl.getSeconds());
            return dto;

        } catch (URISyntaxException e) {
            throw new StorageException("Invalid presigned URL returned by S3", e);

        } catch (S3Exception e) {
            throw new StorageException("Failed to generate presigned URL: " + awsMessage(e), e);
        }
    }

    private FileDto toFileDto(File file) {
        FileDto dto = new FileDto();
        dto.setId(file.getId());
        dto.setFilename(file.getFilename());
        dto.setS3Key(file.getS3Key());
        dto.setContentType(file.getContentType());
        dto.setSize(file.getSize());
        dto.setCreatedAt(toOffsetDateTime(file.getCreatedAt()));
        return dto;
    }

    private static OffsetDateTime toOffsetDateTime(Instant instant) {
        return instant == null ? null : instant.atOffset(ZoneOffset.UTC);
    }

    private void ensureBucketExists(String bucket) {
        try {
            s3.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
        } catch (NoSuchBucketException e) {
            createBucket(bucket);
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                createBucket(bucket);
                return;
            }
            throw new StorageException("Failed to check bucket: " + awsMessage(e), e);
        }
    }

    private void createBucket(String bucket) {
        try {
            s3.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
        } catch (S3Exception e) {
            String code = e.awsErrorDetails() != null ? e.awsErrorDetails().errorCode() : "";
            if ("BucketAlreadyOwnedByYou".equalsIgnoreCase(code) || e.statusCode() == 409) {
                return;
            }
            throw new StorageException("Failed to create bucket: " + awsMessage(e), e);
        }
    }

    private static String sanitizeFilename(String filename) {
        if (filename == null || filename.isBlank()) return "file";
        return filename.replace("\"", "").replace("\r", "").replace("\n", "");
    }

    private static String awsMessage(S3Exception e) {
        if (e.awsErrorDetails() == null) return e.getMessage();
        String msg = e.awsErrorDetails().errorMessage();
        return (msg == null || msg.isBlank()) ? e.getMessage() : msg;
    }
}
