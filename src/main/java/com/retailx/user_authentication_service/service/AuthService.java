package com.retailx.user_authentication_service.service;

import com.retailx.user_authentication_service.dto.LoginRequest;
import com.retailx.user_authentication_service.dto.RegisterRequest;
import com.retailx.user_authentication_service.dto.UserProfileDto;
import com.retailx.user_authentication_service.model.Role;
import com.retailx.user_authentication_service.model.User;
import com.retailx.user_authentication_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // --- 1. REGISTER ---
    public void registerUser(RegisterRequest request) {
        // 1. Check if user already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        // 2. Create User Entity
        User user = new User();
        if(request.getName() != null && !request.getName().equals("")) {
            user.setFirstName(request.getName().getFirstName());
            user.setLastName(request.getName().getLastName());
        }
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 3. Handle Roles (Convert String Strings -> Enums)
        Set<String> strRoles = request.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            roles.add(Role.CUSTOMER); // Default role
        } else {
            strRoles.forEach(role -> {
                try {
                    // Converts "ADMIN" -> Role.ADMIN (Case insensitive safety recommended)
                    roles.add(Role.valueOf(role.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    // If they send a role that doesn't exist, default to CUSTOMER or throw error
                    roles.add(Role.CUSTOMER);
                }
            });
        }
        user.setRoles(roles);

        // 4. Save to DB
        userRepository.save(user);
    }

    // --- 2. LOGIN ---
    // Controller calls: String token = authService.authenticate(loginRequest);
    public String authenticate(LoginRequest request) {
        // 1. Authenticate with Spring Security
        // This will verify the username and password against the DB automatically
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // 2. If no exception was thrown above, the user is valid.
        // Now find the user to get their roles for the token
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 3. Generate and return the JWT Token
        return jwtService.generateToken(user);
    }

    // --- 3. GET PROFILE ---
    // Controller calls: UserProfileDto profile = authService.getUserProfile(username);
    public UserProfileDto getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Convert Enum Roles to List<String> for the DTO
        List<String> roleNames = user.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toList());

        return new UserProfileDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                (Set<String>) roleNames
        );
    }
}