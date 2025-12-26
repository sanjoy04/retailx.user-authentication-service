package com.retailx.user_authentication_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Allow Swagger UI and API Docs
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        // 2. Allow Authentication Endpoints (Register/Login)
                        // CHANGE THIS to match your actual controller path!
                        .requestMatchers("/auth/**", "/register").permitAll()
                        // Keep other endpoints secured
                        .anyRequest().authenticated()
                )
                // Disable CSRF for easier testing via Swagger (optional but recommended for dev)
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}