package com.sdm.property_sale.mapper;

import com.sdm.property_sale.dto.UserRequestDto;
import com.sdm.property_sale.dto.UserResponseDto;
import com.sdm.property_sale.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserMapper {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static User toEntity(UserRequestDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setMobile(dto.getMobile());
        user.setRole(dto.getUserRole());
        user.setActivated(true);
        return user;
    }

    // Convert User entity to UserResponseDto
    public static UserResponseDto toResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId() != null ? user.getId().toString() : null);
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setMobile(user.getMobile());
        dto.setUserRole(user.getRole().name());
        return dto;
    }
}
