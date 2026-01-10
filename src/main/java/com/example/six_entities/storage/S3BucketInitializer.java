package com.example.six_entities.storage;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Component
public class S3BucketInitializer implements ApplicationRunner {

    private final S3Client s3;
    private final S3Properties props;

    public S3BucketInitializer(S3Client s3, S3Properties props) {
        this.s3 = s3;
        this.props = props;
    }

    @Override
    public void run(ApplicationArguments args) {
        String bucket = props.getBucket();

        try {
            s3.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
        } catch (S3Exception e) {
            try {
                s3.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
            } catch (S3Exception ce) {
                String code = ce.awsErrorDetails() != null ? ce.awsErrorDetails().errorCode() : "";
                if (!"BucketAlreadyOwnedByYou".equalsIgnoreCase(code)) {
                    throw ce;
                }
            }
        }
    }
}
