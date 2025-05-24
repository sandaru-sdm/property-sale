package com.sdm.property_sale.dto;

import com.sdm.property_sale.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserRequestDto {
    @Email(message = "Invalid email format")
    private String email;
    private String password;
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Mobile number is mandatory")
    private String mobile;
    private UserRole userRole;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public String getMobile() {
        return mobile;
    }
}
