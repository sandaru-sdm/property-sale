package com.sdm.property_sale.dto;

import com.sdm.property_sale.enums.ImageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PropertyImageDto {
    private Long id;

    @NotBlank(message = "Image path is required")
    private String path;

    @NotNull(message = "Image type is required")
    private ImageType type;

    private Long propertyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ImageType getType() {
        return type;
    }

    public void setType(ImageType type) {
        this.type = type;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }
}