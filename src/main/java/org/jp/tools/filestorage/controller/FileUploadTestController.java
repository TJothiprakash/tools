package org.jp.tools.filestorage.controller;

import lombok.RequiredArgsConstructor;
import org.jp.tools.filestorage.service.WasabiStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class FileUploadTestController {

    private final WasabiStorageService wasabiService;

    @PostMapping("/test")
    public ResponseEntity<String> upload(@RequestParam MultipartFile file) throws Exception {
        System.out.println("inside file upload controller " + file.getName());
        File temp = File.createTempFile("upload", file.getOriginalFilename());
        file.transferTo(temp);
        String key = wasabiService.uploadFile(temp, file.getOriginalFilename());
        return ResponseEntity.ok("Uploaded to Wasabi with key: " + key);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadToWasabi(
            @RequestParam("file") MultipartFile file,
            @RequestParam("email") String email
    ) {
        System.out.println("Controller HIT");
        System.out.println("File: " + file.getOriginalFilename());
        System.out.println("Email: " + email);

        System.out.println("Inside controller"); // add this to test
        return ResponseEntity.ok("Uploaded");
    }

}
