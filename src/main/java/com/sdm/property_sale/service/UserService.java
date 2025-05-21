package com.sdm.property_sale.service;

import com.sdm.property_sale.dto.UserDto;
import com.sdm.property_sale.entity.User;
import com.sdm.property_sale.enums.Status;
import com.sdm.property_sale.enums.UserType;
import com.sdm.property_sale.exception.ForbiddenException;
import com.sdm.property_sale.exception.ResourceNotFoundException;
import com.sdm.property_sale.mapper.UserMapper;
import com.sdm.property_sale.repository.UserRepository;
import com.sdm.property_sale.util.Constants;
import com.sdm.property_sale.util.ValidationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ValidationUtils validationUtils;

    public UserService(UserRepository userRepository, UserMapper userMapper, ValidationUtils validationUtils) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.validationUtils = validationUtils;
    }

    public List<UserDto> getAllUsers() {
        User currentUser = validationUtils.getCurrentUser();

        List<User> users;

        if (currentUser.getUserType() == UserType.SUPER_ADMIN) {
            // Super admin can see all users
            users = userRepository.findAll();
        } else if (currentUser.getUserType() == UserType.ADMIN) {
            // Admin can only see regular users
            users = userRepository.findByUserType(UserType.USER);
        } else {
            // Regular users cannot see other users
            throw new ForbiddenException("Not authorized to view users");
        }

        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(UUID id) {
        User currentUser = validationUtils.getCurrentUser();
        User targetUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND));

        // Super admin can view anyone
        if (currentUser.getUserType() == UserType.SUPER_ADMIN) {
            return userMapper.toDto(targetUser);
        }

        // Admin can only view users
        if (currentUser.getUserType() == UserType.ADMIN && targetUser.getUserType() == UserType.USER) {
            return userMapper.toDto(targetUser);
        }

        // Users can only view themselves
        if (currentUser.getId().equals(targetUser.getId())) {
            return userMapper.toDto(targetUser);
        }

        throw new ForbiddenException("Not authorized to view this user");
    }

    @Transactional
    public UserDto updateUser(UUID id, UserDto userDto) {
        User currentUser = validationUtils.getCurrentUser();
        User targetUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND));

        // Validate update permissions
        validationUtils.validateUpdatePermissions(currentUser, targetUser);

        // Check if email or mobile already exist for another user
        if (!targetUser.getEmail().equals(userDto.getEmail()) &&
                userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException(Constants.DUPLICATE_EMAIL);
        }

        if (!targetUser.getMobile().equals(userDto.getMobile()) &&
                userRepository.existsByMobile(userDto.getMobile())) {
            throw new IllegalArgumentException(Constants.DUPLICATE_MOBILE);
        }

        if (!targetUser.getUsername().equals(userDto.getUsername()) &&
                userRepository.existsByUsername(userDto.getUsername())) {
            throw new IllegalArgumentException(Constants.DUPLICATE_USERNAME);
        }

        // Update and save
        User updatedUser = userMapper.updateEntity(targetUser, userDto);
        updatedUser = userRepository.save(updatedUser);

        return userMapper.toDto(updatedUser);
    }

    @Transactional
    public void changeUserStatus(UUID id, Status status) {
        User currentUser = validationUtils.getCurrentUser();
        User targetUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Constants.USER_NOT_FOUND));

        // Super admin can change status of admins and users
        if (currentUser.getUserType() == UserType.SUPER_ADMIN) {
            if (targetUser.getUserType() == UserType.SUPER_ADMIN && !currentUser.getId().equals(targetUser.getId())) {
                throw new ForbiddenException("Cannot change status of another super admin");
            }
        }
        // Admin can only change status of users
        else if (currentUser.getUserType() == UserType.ADMIN) {
            if (targetUser.getUserType() != UserType.USER) {
                throw new ForbiddenException("Admins can only change status of users");
            }
        }
        // Users cannot change statuses
        else {
            throw new ForbiddenException("Not authorized to change user status");
        }

        targetUser.setStatus(status);
        userRepository.save(targetUser);
    }
}
