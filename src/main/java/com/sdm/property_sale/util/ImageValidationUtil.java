package com.sdm.property_sale.util;

import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.List;

public class ImageValidationUtil {

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    public static boolean isValidImageType(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String contentType = file.getContentType();
        return contentType != null && ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase());
    }

    public static boolean isValidFileSize(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        return file.getSize() <= MAX_FILE_SIZE;
    }

    public static void validateImages(List<MultipartFile> images, String imageType) {
        if (images == null || images.isEmpty()) {
            return;
        }

        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                if (!isValidImageType(image)) {
                    throw new RuntimeException("Invalid " + imageType + " image type. Only JPEG, PNG, GIF, and WebP are allowed.");
                }

                if (!isValidFileSize(image)) {
                    throw new RuntimeException(imageType + " image size cannot exceed 10MB.");
                }
            }
        }
    }
}