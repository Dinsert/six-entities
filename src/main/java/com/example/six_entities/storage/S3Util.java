package com.example.six_entities.storage;

import software.amazon.awssdk.services.s3.model.S3Exception;


public class S3Util {

    public static String awsMessage(S3Exception e) {
        if (e.awsErrorDetails() == null) return e.getMessage();
        String msg = e.awsErrorDetails().errorMessage();
        return (msg == null || msg.isBlank()) ? e.getMessage() : msg;
    }

    public static String sanitizeFilename(String filename) {
        if (filename == null || filename.isBlank()) return "file";
        return filename.replace("\"", "").replace("\r", "").replace("\n", "");
    }
}
