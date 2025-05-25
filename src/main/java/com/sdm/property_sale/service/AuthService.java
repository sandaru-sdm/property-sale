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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.sdm.property_sale.dto.AuthResponseDto; // Added import

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Value("${admin.default.email}")
    private String adminDefaultEmail;

    @Value("${admin.default.name}")
    private String adminDefaultName;

    @Value("${admin.default.mobile}")
    private String adminDefaultMobile;

    @Value("${admin.default.password}")
    private String adminDefaultPassword;

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtUtil jwtUtil, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
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
                AuthResponseDto authResponse = new AuthResponseDto(user.getId(), user.getEmail(), user.getName(), user.getRole(), jwt);
                return ResponseEntity.ok(authResponse);
            } else {
                return ResponseEntity.badRequest().body("Account is not activated.");
            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User not found.");

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
            user.setEmail(adminDefaultEmail);
            user.setName(adminDefaultName);
            user.setMobile(adminDefaultMobile);
            user.setRole(UserRole.SUPER_ADMIN);
            user.setPassword(passwordEncoder.encode(adminDefaultPassword));
            userRepository.save(user);
        } else {
            logger.info("Admin account already exists for email: {}", adminAccount.getEmail());
        }
    }

    public UserResponseDto updateUser(UUID id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID : " + id));
        if (userRequestDto.getEmail() != null && !userRequestDto.getEmail().isBlank()) {
            if (userRepository.existsByEmailAndIdNot(userRequestDto.getEmail(), id)) {
                throw new EmailAlreadyExistsException("A user with this email already exists : " + userRequestDto.getEmail());
            }
            user.setEmail(userRequestDto.getEmail());
        }
        user.setName(userRequestDto.getName());
        user.setMobile(userRequestDto.getMobile());
        user.setRole(userRequestDto.getUserRole());
        if (userRequestDto.getPassword() != null && !userRequestDto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        }
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


    public UserResponseDto getUserById(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with ID : " + userId));;
        return UserMapper.toResponseDto(user);
    }
}
