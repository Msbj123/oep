package com.onlineexam.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for requesting a service-to-service JWT token from the User Service.
 * This is a local copy of the contract sent by the Admin Service.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceTokenRequestDTO {
    private String clientId;
    private String clientSecret;
}
