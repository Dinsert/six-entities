package com.example.six_entities.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3BucketInitializer implements ApplicationRunner {

    private final S3Client s3;
    private final S3Properties props;

    @Override
    public void run(ApplicationArguments args) {
        String bucket = props.getBucket();

        try {
            s3.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
            log.info("S3 bucket '{}' already exists", bucket);
        } catch (NoSuchBucketException e) {
            createBucket(bucket);
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                createBucket(bucket);
                return;
            }
            throw e;
        }
    }

    private void createBucket(String bucket) {
        try {
            s3.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
            log.info("S3 bucket '{}' created", bucket);
        } catch (S3Exception e) {
            String code = e.awsErrorDetails() != null ? e.awsErrorDetails().errorCode() : "";
            if ("BucketAlreadyOwnedByYou".equalsIgnoreCase(code) || e.statusCode() == 409) {
                log.info("S3 bucket '{}' already created by another instance", bucket);
                return;
            }
            throw e;
        }
    }
}
