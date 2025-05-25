package com.sdm.property_sale.controller;

import com.sdm.property_sale.dto.PropertyDto;
import com.sdm.property_sale.enums.PropertyStatus;
import com.sdm.property_sale.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/property")
@CrossOrigin(origins = "*")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("/{propertyId}")
    public ResponseEntity<PropertyDto> getPropertyById(
            @PathVariable Long propertyId) {
        try {
            PropertyDto property = propertyService.getPropertyById(propertyId);
            if (property != null) {
                return ResponseEntity.ok(property);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<PropertyDto> saveProperty(@Valid @RequestBody PropertyDto propertyDto) {
        try {
            PropertyDto savedProperty = propertyService.saveProperty(propertyDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProperty);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/update/{propertyId}")
    public ResponseEntity<PropertyDto> updateProperty(
            @PathVariable Long propertyId,
            @Valid @RequestBody PropertyDto propertyDto) {
        try {
            PropertyDto updatedProperty = propertyService.updateProperty(propertyId, propertyDto);
            return ResponseEntity.ok(updatedProperty);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/update-status")
    public ResponseEntity<PropertyDto> updatePropertyStatus(
            @RequestParam Long propertyId,
            @RequestParam PropertyStatus status,
            @RequestParam UUID userId) {
        try {
            PropertyDto updatedProperty = propertyService.updatePropertyStatus(propertyId, status, userId);
            return ResponseEntity.ok(updatedProperty);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PropertyDto>> getPropertiesByUserId(@PathVariable UUID userId) {
        try {
            List<PropertyDto> properties = propertyService.getPropertiesByUserId(userId);
            return ResponseEntity.ok(properties);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{userId}/property/{propertyId}")
    public ResponseEntity<PropertyDto> getPropertyByIdAndUserId(
            @PathVariable UUID userId,
            @PathVariable Long propertyId) {
        try {
            PropertyDto property = propertyService.getPropertyByIdAndUserId(propertyId, userId);
            if (property != null) {
                return ResponseEntity.ok(property);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteProperty(
            @RequestParam Long propertyId,
            @RequestParam UUID userId) {
        try {
            propertyService.deleteProperty(propertyId, userId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}