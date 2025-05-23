package com.sdm.property_sale.dto;

import com.sdm.property_sale.enums.PropertyStatus;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class PropertyRequestDto {

    @NotBlank(message = "Property name is required")
    @Size(max = 255, message = "Property name cannot exceed 255 characters")
    private String name;

    private String description;

    @NotNull(message = "Land size is required")
    @Positive(message = "Land size must be positive")
    private BigDecimal landSize;

    @Positive(message = "Price per perch must be positive")
    private BigDecimal pricePerPerch;

    @NotNull(message = "Full price is required")
    @Positive(message = "Full price must be positive")
    private BigDecimal fullPrice;

    @NotBlank(message = "Location is required")
    @Size(max = 500, message = "Location cannot exceed 500 characters")
    private String location;

    @NotBlank(message = "Main city is required")
    @Size(max = 100, message = "Main city cannot exceed 100 characters")
    private String mainCity;

    @NotBlank(message = "Distance to main city is required")
    @Size(max = 100, message = "Distance to main city cannot exceed 100 characters")
    private String distanceToMainCity;

    @NotBlank(message = "Owner name is required")
    @Size(max = 255, message = "Owner name cannot exceed 255 characters")
    private String ownerName;

    @NotBlank(message = "Owner mobile is required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid mobile number format")
    private String ownerMobile;

    @Size(max = 500, message = "Owner address cannot exceed 500 characters")
    private String ownerAddress;

    @NotNull(message = "Owner price is required")
    @Positive(message = "Owner price must be positive")
    private BigDecimal ownerPrice;

    private PropertyStatus status;

    public PropertyStatus getStatus() {
        return status;
    }

    public void setStatus(PropertyStatus status) {
        this.status = status;
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
}