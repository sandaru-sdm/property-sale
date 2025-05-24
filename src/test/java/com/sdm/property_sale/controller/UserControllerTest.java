package com.sdm.property_sale.controller;

import com.sdm.property_sale.dto.UserRequestDto;
import com.sdm.property_sale.dto.UserResponseDto;
import com.sdm.property_sale.enums.UserRole;
import com.sdm.property_sale.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private UserController userController;

    @Test
    void signUpUser_whenUserRegistered_returnsCreatedStatusAndUserDto() {
        // Arrange
        UserRequestDto requestDto = new UserRequestDto(); 
        // Populate requestDto if its fields are used by authService.createUser in a way that affects the test outcome.
        // For this specific test, it's mainly a pass-through object to the mock.

        UserResponseDto expectedResponseDto = new UserResponseDto();
        String expectedId = UUID.randomUUID().toString();
        String expectedEmail = "test@example.com";
        String expectedName = "Test User";
        String expectedMobile = "1234567890";
        String expectedRole = UserRole.USER.toString(); // Assuming UserRole is an enum

        expectedResponseDto.setId(expectedId);
        expectedResponseDto.setEmail(expectedEmail);
        expectedResponseDto.setName(expectedName);
        expectedResponseDto.setMobile(expectedMobile);
        expectedResponseDto.setUserRole(expectedRole);


        when(authService.createUser(any(UserRequestDto.class))).thenReturn(expectedResponseDto);

        // Act
        ResponseEntity<UserResponseDto> responseEntity = userController.signUpUser(requestDto);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedResponseDto, responseEntity.getBody());
        // Verify specific fields if direct object comparison is problematic due to lack of equals/hashCode
        assertNotNull(responseEntity.getBody());
        assertEquals(expectedId, responseEntity.getBody().getId());
        assertEquals(expectedEmail, responseEntity.getBody().getEmail());
        assertEquals(expectedName, responseEntity.getBody().getName());
        assertEquals(expectedMobile, responseEntity.getBody().getMobile());
        assertEquals(expectedRole, responseEntity.getBody().getUserRole());
        
        verify(authService).createUser(requestDto);
    }

    // Add other tests for UserController as needed for other endpoints in the future.
}
