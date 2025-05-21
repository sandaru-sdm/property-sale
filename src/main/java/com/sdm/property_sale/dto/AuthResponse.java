package com.sdm.property_sale.dto;

import com.sdm.property_sale.enums.UserType;
import java.util.UUID;

public class AuthResponse {
    private String token;
    private UUID id;
    private String username;
    private UserType userType;

    public AuthResponse() {
    }

    public AuthResponse(String token, UUID id, String username, UserType userType) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.userType = userType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
