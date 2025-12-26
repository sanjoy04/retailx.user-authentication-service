package com.retailx.user_authentication_service.controller;

import com.retailx.user_authentication_service.dto.AuthResponse;
import com.retailx.user_authentication_service.dto.LoginRequest;
import com.retailx.user_authentication_service.dto.RegisterRequest;
import com.retailx.user_authentication_service.dto.UserProfileDto;
import com.retailx.user_authentication_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest){
        authService.registerUser(registerRequest);
        return ResponseEntity.ok("User Registration Successful");
    }
    @PostMapping("login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        String token = authService.authenticate(loginRequest);
        return ResponseEntity.ok(new AuthResponse(token));
    }
    @PostMapping("logout")
    public ResponseEntity<String> logout(){
        return ResponseEntity.ok("Logged out successfully. Please discard the token client-side.");
    }
    @GetMapping("me")
    public ResponseEntity<UserProfileDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        // @AuthenticationPrincipal injects the currently logged-in user from the security context
        UserProfileDto profile = authService.getUserProfile(userDetails.getUsername());
        return ResponseEntity.ok(profile);
    }
}
