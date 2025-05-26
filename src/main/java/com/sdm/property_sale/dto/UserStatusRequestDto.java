package com.sdm.property_sale.dto;

import jakarta.validation.constraints.NotNull;

public class UserStatusRequestDto {
    @NotNull(message = "User ID cannot be null")
    private String userId;
    @NotNull(message = "Status cannot be null")
    private String status;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}