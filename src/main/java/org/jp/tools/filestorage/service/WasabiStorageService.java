package org.jp.tools.filestorage.service;

import lombok.RequiredArgsConstructor;
import org.jp.tools.filestorage.checksum.ChecksumService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WasabiStorageService {

    private final S3Client s3Client;
    private final ChecksumService checksumService;

    @Value("${wasabi.bucket-name}")
    private String bucketName;

    public void uploadChunk(Path chunkPath, String objectKey, String clientChecksum) {
        try (InputStream is = new FileInputStream(chunkPath.toFile())) {
            if (!checksumService.validateChecksum(is, clientChecksum)) {
                throw new RuntimeException("Checksum mismatch! Upload corrupted.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read chunk for checksum validation", e);
        }

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        s3Client.putObject(request, RequestBody.fromFile(chunkPath));
    }
/*

    public String uploadFile(File file, String originalFilename) {
        try {
            String key = "uploads/" + UUID.randomUUID() + "_" + originalFilename;

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.putObject(request, RequestBody.fromFile(file));
            System.out.println("Uploaded file to Wasabi: " + file.getName());

            return key;

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to Wasabi", e);
        }
    }
*/

    public String uploadFile(File file, String originalFilename, String clientChecksum) {
        try {
            // Step 1: Validate checksum
            if (!checksumService.validateChecksum(new FileInputStream(file), clientChecksum)) {
                throw new RuntimeException("Checksum mismatch! Upload corrupted.");
            }

            // Step 2: Generate a key
            String key = "uploads/" + UUID.randomUUID() + "_" + originalFilename;

            // Step 3: Create and send the upload request
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.putObject(request, RequestBody.fromFile(file));

            System.out.println("inside WasabiStorageService file upload completed: " + file.getName());
            return key;

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to Wasabi", e);
        }
    }

}
