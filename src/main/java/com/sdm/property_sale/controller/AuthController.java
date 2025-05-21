package com.sdm.property_sale.controller;
import com.sdm.property_sale.dto.AuthRequest;
import com.sdm.property_sale.dto.AuthResponse;
import com.sdm.property_sale.dto.UserRegistrationDto;
import com.sdm.property_sale.service.AuthService;
import com.sdm.property_sale.util.Constants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.AUTH_PATH)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/register/super-admin")
    public ResponseEntity<Void> registerSuperAdmin(@Valid @RequestBody UserRegistrationDto registrationDto) {
        authService.registerSuperAdmin(registrationDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        authService.registerUser(registrationDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
