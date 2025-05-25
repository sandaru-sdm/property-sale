package com.sdm.property_sale.dto;

import com.sdm.property_sale.enums.UserRole;

import java.util.UUID;

public class AuthResponseDto {

    private final UUID userId;
    private final String email;
    private final String name;
    private final UserRole userRole;
    private final String token;

    public AuthResponseDto(UUID userId, String email, String name, UserRole userRole, String token) {
        this.userId = userId;
        this.email = email;
        this.name = name;
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
        return "Bearer";
    }
    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }
}
