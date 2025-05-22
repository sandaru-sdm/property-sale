package com.sdm.property_sale.service;

import com.sdm.property_sale.dto.AuthenticationRequest;
import com.sdm.property_sale.dto.UserRequestDto;
import com.sdm.property_sale.dto.UserResponseDto;
import com.sdm.property_sale.entity.User;
import com.sdm.property_sale.enums.UserRole;
import com.sdm.property_sale.exception.EmailAlreadyExistsException;
import com.sdm.property_sale.exception.UserNotFoundException;
import com.sdm.property_sale.mapper.UserMapper;
import com.sdm.property_sale.repository.UserRepository;
import com.sdm.property_sale.util.JwtUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.json.JSONObject;
import org.json.JSONException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    public List<UserResponseDto> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserMapper::toResponseDto).toList();
    }

    public ResponseEntity<?> authenticate(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Incorrect username or password.");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        Optional<User> optionalUser = userRepository.findByEmail(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserRole role = user.getRole();
            String jwt = jwtUtil.generateToken(userDetails.getUsername(), role.toString());

            if (user.isActivated()) {
                try {
                    JSONObject response = new JSONObject();
                    response.put("userId", user.getId());
                    response.put("userRole", user.getRole());
                    response.put("token", jwt);
                    response.put("tokenType", "Bearer");
                    return ResponseEntity.ok(response.toString());
                } catch (JSONException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while creating response.");
                }
            } else {
                return ResponseEntity.badRequest().body("Account is not activated. Check your email.");
            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User not found.");

    }

    public Boolean hasUserWithEmail(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    public UserResponseDto createUser(UserRequestDto userRegisterRequest){

        if(userRepository.existsByEmail(userRegisterRequest.getEmail())) {
            throw new EmailAlreadyExistsException("A user with this email already exists : " +userRegisterRequest.getEmail());
        }

        User newUser = userRepository.save(UserMapper.toEntity(userRegisterRequest));
        return UserMapper.toResponseDto(newUser);
    }

    @PostConstruct
    public void createAdminAccount(){
        User adminAccount = userRepository.findByRole(UserRole.SUPER_ADMIN);
        if(adminAccount == null){
            User user = new User();
            user.setEmail("maduhansadilshan@gmail.com");
            user.setName("Sandaru Gunathilake");
            user.setMobile("0701794934");
            user.setRole(UserRole.SUPER_ADMIN);
            user.setPassword(new BCryptPasswordEncoder().encode("dilshan2000"));
            userRepository.save(user);
        } else {
            System.out.println("Admin Account is exist." + adminAccount);
        }
    }

    public UserResponseDto updateUser(UUID id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with ID : " + id));
        if(userRepository.existsByEmailAndIdNot(userRequestDto.getEmail(), id)) {
            throw new EmailAlreadyExistsException("A user with this email already exists : " + userRequestDto.getEmail());
        }
        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        user.setMobile(userRequestDto.getMobile());
        user.setRole(userRequestDto.getUserRole());
        User updatedUser = userRepository.save(user);
        return UserMapper.toResponseDto(updatedUser);
    }

    public UserResponseDto updateStatus(UUID id, boolean isActivated) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID : " + id));
        user.setActivated(isActivated);
        User updatedUser = userRepository.save(user);
        return UserMapper.toResponseDto(updatedUser);
    }


}
