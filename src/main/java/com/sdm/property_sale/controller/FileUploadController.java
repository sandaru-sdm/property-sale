package com.sdm.property_sale.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/upload")
@CrossOrigin(origins = "*")
public class FileUploadController {

    @Value("${file.upload.directory:uploads}")
    private String uploadDirectory;

    @PostMapping("/property-images")
    public ResponseEntity<Map<String, Object>> uploadPropertyImages(
            @RequestParam("landImages") MultipartFile[] landImages,
            @RequestParam("planImages") MultipartFile[] planImages) {

        Map<String, Object> response = new HashMap<>();
        List<String> landImagePaths = new ArrayList<>();
        List<String> planImagePaths = new ArrayList<>();

        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Upload land images
            setImages(landImages, landImagePaths, uploadPath);

            // Upload plan images
            setImages(planImages, planImagePaths, uploadPath);

            response.put("landImages", landImagePaths);
            response.put("planImages", planImagePaths);
            response.put("message", "Files uploaded successfully");

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            response.put("error", "Failed to upload files: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private void setImages(MultipartFile[] files, List<String> imagePaths, Path uploadPath) throws IOException {
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath);
                imagePaths.add(fileName);
            }
        }
    }
}