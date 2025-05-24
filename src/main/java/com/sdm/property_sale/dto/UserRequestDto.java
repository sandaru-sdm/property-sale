package com.sdm.property_sale.dto;

import com.sdm.property_sale.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserRequestDto {
    @Email(message = "Invalid email format")
    private String email;
    // @NotBlank was removed here.
    @Size(min = 8, message = "Password must be at least 8 characters long if provided")
    private String password;
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Mobile number is mandatory")
    private String mobile;
    @NotNull(message = "User role is mandatory")
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
