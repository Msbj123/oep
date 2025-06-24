package com.OnlineExamPortal.UserModule.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for requesting a service-to-service JWT token.
 * Contains client credentials (client ID and client secret).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceTokenRequestDTO {
    @NotBlank(message = "Client ID cannot be blank")
    private String clientId;

    @NotBlank(message = "Client Secret cannot be blank")
    private String clientSecret;
}
