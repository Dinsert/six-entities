package com.example.six_entities.storage;

import com.example.six_entities.exception.FileUploadException;
import com.example.six_entities.exception.StorageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3StorageImpl implements S3Storage {

    private final S3Client s3;
    private final S3Presigner presigner;
    private final S3Properties props;

    @Override
    public void putObject(String key, MultipartFile file, String contentType) {
        PutObjectRequest putReq = PutObjectRequest.builder()
                .bucket(props.getBucket())
                .key(key)
                .contentType(contentType)
                .contentLength(file.getSize())
                .build();

        try (InputStream inputStream = file.getInputStream()) {
            s3.putObject(putReq, RequestBody.fromInputStream(inputStream, file.getSize()));
        } catch (IOException e) {
            log.error("Failed to read upload stream for key={}", key, e);
            throw new FileUploadException("Failed to read upload stream", e);
        } catch (S3Exception e) {
            log.error("S3 upload failed: key={}, error={}", key, S3Util.awsMessage(e), e);
            throw new StorageException("Failed to upload to S3: " + S3Util.awsMessage(e), e);
        }
    }

    @Override
    public String presignGetUrl(String key, String downloadFilename) {
        GetObjectRequest getReq = GetObjectRequest.builder()
                .bucket(props.getBucket())
                .key(key)
                .responseContentDisposition("attachment; filename=\"" + downloadFilename + "\"")
                .build();

        try {
            PresignedGetObjectRequest presigned = presigner.presignGetObject(
                    GetObjectPresignRequest.builder()
                            .signatureDuration(props.getPresignTtl())
                            .getObjectRequest(getReq)
                            .build()
            );
            return presigned.url().toString();
        } catch (S3Exception e) {
            log.error("Failed to create presigned URL for key={}, error={}", key, S3Util.awsMessage(e), e);
            throw new StorageException("Failed to generate presigned URL: " + S3Util.awsMessage(e), e);
        }
    }

    @Override
    public void deleteQuietly(String key) {
        try {
            s3.deleteObject(DeleteObjectRequest.builder()
                    .bucket(props.getBucket())
                    .key(key)
                    .build());
        } catch (S3Exception e) {
            log.error("Failed to rollback S3 object: key={}, error={}", key, S3Util.awsMessage(e), e);
        }
    }
}