package com.sdm.property_sale.dto;

import com.sdm.property_sale.enums.PropertyStatus;
import jakarta.validation.constraints.NotNull;

public class PropertyStatusUpdateDto {

    @NotNull(message = "Status is required")
    private PropertyStatus status;


    public PropertyStatus getStatus() {
        return status;
    }

    public void setStatus(PropertyStatus status) {
        this.status = status;
    }
}