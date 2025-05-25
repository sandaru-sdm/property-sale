package com.sdm.property_sale.controller;

import com.sdm.property_sale.dto.UserRequestDto;
import com.sdm.property_sale.dto.UserResponseDto;
import com.sdm.property_sale.dto.UserStatusDto;
import com.sdm.property_sale.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
@Validated
public class UserController {
    private final AuthService authService;

    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable String userId) {
        UserResponseDto user = authService.getUserById(UUID.fromString(userId));
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        List<UserResponseDto> users = authService.getUsers();
        return ResponseEntity.ok().body(users);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> signUpUser(@Valid @RequestBody UserRequestDto userRegisterRequest){
        UserResponseDto userDto = authService.createUser(userRegisterRequest);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable UUID id, @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto patientResponseDTO = authService.updateUser(id, userRequestDto);
        return ResponseEntity.ok().body(patientResponseDTO);
    }

    @PatchMapping("status/{id}")
    public ResponseEntity<UserResponseDto> updateStatus(@PathVariable UUID id, @RequestBody UserStatusDto statusDto) {
        UserResponseDto response = authService.updateStatus(id, statusDto.isActivated());
        return ResponseEntity.ok().body(response);
    }

}
