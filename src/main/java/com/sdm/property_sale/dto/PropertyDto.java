package com.sdm.property_sale.dto;

import com.sdm.property_sale.enums.PropertyStatus;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PropertyDto {
    private Long id;

    @NotBlank(message = "Property name is required")
    @Size(max = 255, message = "Property name must not exceed 255 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Positive(message = "Land size must be positive")
    private BigDecimal landSize;

    @Positive(message = "Price per perch must be positive")
    private BigDecimal pricePerPerch;

    @NotNull(message = "Full price is required")
    @Positive(message = "Full price must be positive")
    private BigDecimal fullPrice;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Main city is required")
    private String mainCity;

    private String distanceToMainCity;

    @NotBlank(message = "Owner name is required")
    private String ownerName;

    @NotBlank(message = "Owner mobile is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Owner mobile must be 10 digits")
    private String ownerMobile;

    private String ownerAddress;

    @Positive(message = "Owner price must be positive")
    private BigDecimal ownerPrice;

    private PropertyStatus status;

    @NotNull(message = "User ID is required")
    private UUID userId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<PropertyImageDto> images;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getLandSize() {
        return landSize;
    }

    public void setLandSize(BigDecimal landSize) {
        this.landSize = landSize;
    }

    public BigDecimal getPricePerPerch() {
        return pricePerPerch;
    }

    public void setPricePerPerch(BigDecimal pricePerPerch) {
        this.pricePerPerch = pricePerPerch;
    }

    public BigDecimal getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(BigDecimal fullPrice) {
        this.fullPrice = fullPrice;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMainCity() {
        return mainCity;
    }

    public void setMainCity(String mainCity) {
        this.mainCity = mainCity;
    }

    public String getDistanceToMainCity() {
        return distanceToMainCity;
    }

    public void setDistanceToMainCity(String distanceToMainCity) {
        this.distanceToMainCity = distanceToMainCity;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerMobile() {
        return ownerMobile;
    }

    public void setOwnerMobile(String ownerMobile) {
        this.ownerMobile = ownerMobile;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    public BigDecimal getOwnerPrice() {
        return ownerPrice;
    }

    public void setOwnerPrice(BigDecimal ownerPrice) {
        this.ownerPrice = ownerPrice;
    }

    public PropertyStatus getStatus() {
        return status;
    }

    public void setStatus(PropertyStatus status) {
        this.status = status;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<PropertyImageDto> getImages() {
        return images;
    }

    public void setImages(List<PropertyImageDto> images) {
        this.images = images;
    }
}