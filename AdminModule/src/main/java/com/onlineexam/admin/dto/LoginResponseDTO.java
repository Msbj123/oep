package com.onlineexam.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for the response from the User Service's login or service token endpoint.
 * This is a local copy of the contract expected by the Admin Service.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String email; // Or clientId for service tokens
    private String message;
}