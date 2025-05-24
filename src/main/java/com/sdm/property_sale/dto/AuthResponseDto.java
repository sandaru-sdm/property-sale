package com.sdm.property_sale.dto;

import com.sdm.property_sale.enums.UserRole;

import java.util.UUID;

public class AuthResponseDto {

    private UUID userId;
    private UserRole userRole;
    private String token;
    private String tokenType = "Bearer"; // Default initialization

    public AuthResponseDto(UUID userId, UserRole userRole, String token) {
        this.userId = userId;
        this.userRole = userRole;
        this.token = token;
    }

    public UUID getUserId() {
        return userId;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }
}
