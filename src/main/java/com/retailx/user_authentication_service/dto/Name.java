package com.retailx.user_authentication_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Name {
    @NotBlank(message = "Firstname is required")
    private String firstName;
    private String lastName;
}
