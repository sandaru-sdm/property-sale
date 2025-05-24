package com.sdm.property_sale.service;

import com.sdm.property_sale.dto.AuthResponseDto;
import com.sdm.property_sale.dto.AuthenticationRequest;
import com.sdm.property_sale.dto.UserRequestDto;
import com.sdm.property_sale.entity.User;
import com.sdm.property_sale.enums.UserRole;
import com.sdm.property_sale.repository.UserRepository;
import com.sdm.property_sale.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils; // For @Value fields

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private final String adminEmail = "testadmin@example.com";
    private final String adminName = "Test Admin";
    private final String adminMobile = "1234567890";
    private final String adminPassword = "password123"; // Raw password

    @BeforeEach
    void setUp() {
        // Manually set @Value fields for testing createAdminAccount
        ReflectionTestUtils.setField(authService, "adminDefaultEmail", adminEmail);
        ReflectionTestUtils.setField(authService, "adminDefaultName", adminName);
        ReflectionTestUtils.setField(authService, "adminDefaultMobile", adminMobile);
        ReflectionTestUtils.setField(authService, "adminDefaultPassword", adminPassword);
    }

    @Test
    void createAdminAccount_whenNoAdminExists_createsAdmin() {
        // Arrange
        when(userRepository.findByRole(UserRole.SUPER_ADMIN)).thenReturn(null);
        when(passwordEncoder.encode(adminPassword)).thenReturn("encodedPassword");
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // Act
        authService.createAdminAccount(); // This is @PostConstruct, normally called by Spring. Call manually for test.

        // Assert
        verify(userRepository).findByRole(UserRole.SUPER_ADMIN);
        verify(passwordEncoder).encode(adminPassword);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals(adminEmail, savedUser.getEmail());
        assertEquals(adminName, savedUser.getName());
        assertEquals(adminMobile, savedUser.getMobile());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals(UserRole.SUPER_ADMIN, savedUser.getRole());
        assertTrue(savedUser.isActivated()); // Default is true as confirmed in User entity
    }

    @Test
    void createAdminAccount_whenAdminExists_doesNotCreateNewAdmin() {
        // Arrange
        User existingAdmin = new User();
        existingAdmin.setEmail(adminEmail); // Set some property to identify it for logging
        when(userRepository.findByRole(UserRole.SUPER_ADMIN)).thenReturn(existingAdmin);

        // Act
        authService.createAdminAccount();

        // Assert
        verify(userRepository).findByRole(UserRole.SUPER_ADMIN);
        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void updateUser_whenPasswordChanged_encodesNewPassword() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UserRequestDto mockDto = mock(UserRequestDto.class);
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("originalemail@example.com");
        existingUser.setPassword("oldEncodedPassword");

        // Define behavior for the mock DTO
        when(mockDto.getPassword()).thenReturn("newRawPassword"); // New password provided
        when(mockDto.getEmail()).thenReturn("originalemail@example.com"); // Assume email is not changing or valid
        when(mockDto.getName()).thenReturn("Updated Name");
        when(mockDto.getMobile()).thenReturn("0987654321");
        when(mockDto.getUserRole()).thenReturn(UserRole.USER);


        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newRawPassword")).thenReturn("newEncodedPassword");
        // Assuming email is not being changed or if it is, it doesn't clash
        when(userRepository.existsByEmailAndIdNot(mockDto.getEmail(), userId)).thenReturn(false);


        // Act
        authService.updateUser(userId, mockDto);

        // Assert
        verify(userRepository).findById(userId);
        verify(passwordEncoder).encode("newRawPassword");
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals("newEncodedPassword", savedUser.getPassword());
        assertEquals("Updated Name", savedUser.getName()); // Also verify other fields are updated
        assertEquals("0987654321", savedUser.getMobile());
        assertEquals(UserRole.USER, savedUser.getRole());
    }

    @Test
    void updateUser_whenPasswordNotProvided_doesNotEncodeOrUpdatePassword() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UserRequestDto mockDto = mock(UserRequestDto.class);
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setPassword("oldEncodedPassword"); // Existing password

        // Simulate DTO without a new password
        when(mockDto.getPassword()).thenReturn(null); // Or "" or "   "
        when(mockDto.getEmail()).thenReturn("someemail@example.com");
        when(mockDto.getName()).thenReturn("Some Name Updated"); // Other fields can be updated
        when(mockDto.getMobile()).thenReturn("321321321");
        when(mockDto.getUserRole()).thenReturn(UserRole.USER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmailAndIdNot(anyString(), eq(userId))).thenReturn(false);
        // No expectation for passwordEncoder.encode() to be called

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(userCaptor.capture())).thenReturn(existingUser); // Capture the user passed to save

        // Act
        authService.updateUser(userId, mockDto);

        // Assert
        verify(userRepository).findById(userId);
        verify(passwordEncoder, never()).encode(anyString()); // Ensure encoder is not called
        verify(userRepository).save(any(User.class));
        
        User savedUser = userCaptor.getValue();
        assertEquals("oldEncodedPassword", savedUser.getPassword()); // Password should remain unchanged
        assertEquals("Some Name Updated", savedUser.getName()); // Other fields should be updated
    }
    
    @Test
    void authenticate_validCredentialsAndActiveUser_returnsAuthResponseDto() {
        // Arrange
        AuthenticationRequest authRequest = new AuthenticationRequest("user@example.com", "password");
        UserDetails mockUserDetails = mock(UserDetails.class); // UserDetails from Spring Security
        User mockAppUser = new User(); // Your application's User entity
        UUID userId = UUID.randomUUID();
        mockAppUser.setId(userId);
        mockAppUser.setRole(UserRole.USER);
        mockAppUser.setActivated(true);
        mockAppUser.setEmail(authRequest.getEmail()); // Ensure email matches
        String fakeJwt = "fake.jwt.token";

        // Mocking Spring Security flow
        // 1. AuthenticationManager successfully authenticates
        // (No specific return value, but no exception thrown)
        doNothing().when(authenticationManager).authenticate(
            new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        // 2. UserDetailsService loads user
        when(userDetailsService.loadUserByUsername(authRequest.getEmail())).thenReturn(mockUserDetails);
        when(mockUserDetails.getUsername()).thenReturn(authRequest.getEmail()); // Important for retrieving app user

        // 3. UserRepository finds the application user
        when(userRepository.findByEmail(authRequest.getEmail())).thenReturn(Optional.of(mockAppUser));
        
        // 4. JwtUtil generates token
        when(jwtUtil.generateToken(authRequest.getEmail(), UserRole.USER.toString())).thenReturn(fakeJwt);

        // Act
        ResponseEntity<?> responseEntity = authService.authenticate(authRequest);

        // Assert
        // Verify interactions
        verify(authenticationManager).authenticate(
            new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
        verify(userDetailsService).loadUserByUsername(authRequest.getEmail());
        verify(userRepository).findByEmail(authRequest.getEmail());
        verify(jwtUtil).generateToken(authRequest.getEmail(), UserRole.USER.toString());

        // Verify response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof AuthResponseDto);
        AuthResponseDto responseBody = (AuthResponseDto) responseEntity.getBody();
        assertEquals(userId, responseBody.getUserId());
        assertEquals(UserRole.USER, responseBody.getUserRole());
        assertEquals(fakeJwt, responseBody.getToken());
        assertEquals("Bearer", responseBody.getTokenType());
    }
}
