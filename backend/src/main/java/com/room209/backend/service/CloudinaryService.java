package com.room209.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private static final Logger logger = LoggerFactory.getLogger(CloudinaryService.class);
    private final Cloudinary cloudinary;

    public CloudinaryService(
            @Value("${cloudinary.cloud-name:demo}") String cloudName,
            @Value("${cloudinary.api-key:sample_key}") String apiKey,
            @Value("${cloudinary.api-secret:sample_secret}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true
        ));
    }

    public String uploadFile(MultipartFile file, String folder) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "room209/" + folder,
                    "resource_type", "auto"
            ));
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            logger.error("Failed to upload image to Cloudinary: {}", e.getMessage());
            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
    }

    public Map<String, Object> generateUploadSignature(String folder) {
        long timestamp = System.currentTimeMillis() / 1000L;
        Map<String, Object> params = ObjectUtils.asMap(
                "folder", "room209/" + folder,
                "timestamp", timestamp
        );
        try {
            String signature = cloudinary.apiSignRequest(params, cloudinary.config.apiSecret);
            params.put("signature", signature);
            params.put("api_key", cloudinary.config.apiKey);
            params.put("cloud_name", cloudinary.config.cloudName);
            return params;
        } catch (Exception e) {
            logger.error("Failed to generate Cloudinary signature: {}", e.getMessage());
            throw new RuntimeException("Signature generation failed: " + e.getMessage());
        }
    }
}
