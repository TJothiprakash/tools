package org.jp.tools.filestorage.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WasabiStorageService {

    private final S3Client s3Client;
    @Value("${wasabi.bucket-name}")
    private String bucketName;

//    private final String bucketName = "your-bucket-name";

    public void uploadChunk(Path chunkPath, String objectKey) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        s3Client.putObject(request, RequestBody.fromFile(chunkPath));
    }

    public String uploadFile(File file, String originalFilename) {
        try {
            String key = "uploads/" + UUID.randomUUID() + "_" + originalFilename;

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.putObject(request, RequestBody.fromFile(file));
            return key;

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to Wasabi", e);
        }
    }
}
