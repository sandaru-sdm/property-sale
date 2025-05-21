package com.sdm.property_sale.service;

import com.sdm.property_sale.dto.AuthRequest;
import com.sdm.property_sale.dto.AuthResponse;
import com.sdm.property_sale.dto.UserRegistrationDto;
import com.sdm.property_sale.entity.User;
import com.sdm.property_sale.enums.Status;
import com.sdm.property_sale.enums.UserType;
import com.sdm.property_sale.mapper.UserMapper;
import com.sdm.property_sale.repository.UserRepository;
import com.sdm.property_sale.util.Constants;
import com.sdm.property_sale.util.ValidationUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final ValidationUtils validationUtils;

    public AuthService(UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager, UserMapper userMapper, ValidationUtils validationUtils) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
        this.validationUtils = validationUtils;
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        com.sdm.property_sale.entity.User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(Constants.USER_NOT_FOUND));

        if (user.getStatus() != Status.ACTIVE) {
            throw new IllegalArgumentException("User account is inactive");
        }

        String jwtToken = jwtService.generateToken(user);

        AuthResponse response = new AuthResponse();
        response.setToken(jwtToken);
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setUserType(user.getUserType());
        return response;

    }

    @Transactional
    public void registerSuperAdmin(UserRegistrationDto registrationDto) {
        // Validate for duplicates
        validationUtils.checkForDuplicates(
                registrationDto.getUsername(),
                registrationDto.getEmail(),
                registrationDto.getMobile()
        );

        // Set user type to SUPER_ADMIN
        registrationDto.setUserType(UserType.SUPER_ADMIN);

        // Convert to entity and save
        com.sdm.property_sale.entity.User user = userMapper.toEntity(registrationDto);
        userRepository.save(user);
    }

    @Transactional
    public void registerUser(UserRegistrationDto registrationDto) {
        // Get current user
        com.sdm.property_sale.entity.User currentUser = validationUtils.getCurrentUser();

        // Validate permissions
        validationUtils.validateRegistrationPermissions(currentUser, registrationDto.getUserType());

        // Validate for duplicates
        validationUtils.checkForDuplicates(
                registrationDto.getUsername(),
                registrationDto.getEmail(),
                registrationDto.getMobile()
        );

        // Convert to entity and save
        User user = userMapper.toEntity(registrationDto);
        userRepository.save(user);
    }
}
