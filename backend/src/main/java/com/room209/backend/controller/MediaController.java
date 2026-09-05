package com.room209.backend.controller;

import com.room209.backend.service.CloudinaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/media")
public class MediaController {

    private final CloudinaryService cloudinaryService;

    public MediaController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadMedia(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "feed") String folder) {
        String url = cloudinaryService.uploadFile(file, folder);
        return ResponseEntity.ok(Map.of("url", url));
    }

    @GetMapping("/signature")
    public ResponseEntity<Map<String, Object>> getUploadSignature(
            @RequestParam(value = "folder", defaultValue = "feed") String folder) {
        return ResponseEntity.ok(cloudinaryService.generateUploadSignature(folder));
    }
}
