package com.sdm.property_sale.controller;

import com.sdm.property_sale.dto.PropertyDto;
import com.sdm.property_sale.service.PropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public")
@CrossOrigin(origins = "*")
public class PublicController {

    private final PropertyService propertyService;

    public PublicController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<PropertyDto>> getPublicPropertiesByUserId(@PathVariable UUID userId) {
        try {
            List<PropertyDto> properties = propertyService.getPropertiesByUserId(userId);
            return ResponseEntity.ok(properties);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{userId}/{propertyId}")
    public ResponseEntity<PropertyDto> getPublicPropertyById(
            @PathVariable UUID userId,
            @PathVariable Long propertyId) {
        try {
            PropertyDto property = propertyService.getPropertyById(propertyId);
            if (property != null && property.getUserId().equals(userId)) {
                return ResponseEntity.ok(property);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}