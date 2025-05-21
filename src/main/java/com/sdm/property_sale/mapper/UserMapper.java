package com.sdm.property_sale.mapper;

import com.sdm.property_sale.dto.UserDto;
import com.sdm.property_sale.dto.UserRegistrationDto;
import com.sdm.property_sale.entity.User;
import com.sdm.property_sale.enums.Status;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setMobile(user.getMobile());
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        userDto.setUserType(user.getUserType());
        userDto.setStatus(user.getStatus());
        return userDto;
    }

    public User toEntity(UserRegistrationDto registrationDto) {
        User user = new User();
        user.setName(registrationDto.getName());
        user.setMobile(registrationDto.getMobile());
        user.setEmail(registrationDto.getEmail());
        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setUserType(registrationDto.getUserType());
        user.setStatus(Status.ACTIVE);
        return user;
    }

    public User updateEntity(User existingUser, UserDto userDto) {
        existingUser.setName(userDto.getName());
        existingUser.setMobile(userDto.getMobile());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setUsername(userDto.getUsername());
        existingUser.setStatus(userDto.getStatus());
        // We don't update user type and password here
        return existingUser;
    }
}
