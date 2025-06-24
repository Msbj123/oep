package com.onlineexam.admin.client;


import com.onlineexam.admin.Exception.CustomException; // Corrected import casing
import com.onlineexam.admin.dto.LoginResponseDTO; // Changed to local DTO
import com.onlineexam.admin.dto.ServiceTokenRequestDTO; // Changed to local DTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity; 

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Component responsible for fetching and providing a valid service-to-service JWT token.
 * It dynamically requests a token from the User Service's /token/service endpoint
 * using predefined client credentials, and refreshes it when it's near expiration.
 */
@Component
public class TokenProvider {

    @Value("${user-service.url}")
    private String userServiceUrl;

    @Value("${admin-service.client-id}")
    private String clientId;

    @Value("${admin-service.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    private String currentToken;
    private Instant tokenExpirationTime;

    @Autowired
    public TokenProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Provides a valid (non-expired) JWT token. If the current token is null or
     * near expiration, it will request a new one from the User Service.
     * @return A valid JWT token string.
     * @throws CustomException if token generation fails.
     */
    public String getValidToken() {
        // Check if token is null or expires within the next 5 minutes
        if (currentToken == null || tokenExpirationTime == null || tokenExpirationTime.minus(5, ChronoUnit.MINUTES).isBefore(Instant.now())) {
            System.out.println("DEBUG: Token is null or near expiration. Requesting new token...");
            fetchNewToken();
        }
        return currentToken;
    }

    /**
     * Fetches a new service token from the User Service using client credentials.
     * Stores the token and its expiration time.
     * @throws CustomException if the User Service returns an error.
     */
    private synchronized void fetchNewToken() {
        // Double-check locking to prevent multiple threads from fetching concurrently
        if (currentToken != null && tokenExpirationTime != null && tokenExpirationTime.minus(5, ChronoUnit.MINUTES).isAfter(Instant.now())) {
            return; // Another thread already fetched a new token
        }

        String tokenEndpoint = userServiceUrl + "/examProtal/userModule/token/service";
        ServiceTokenRequestDTO requestDTO = new ServiceTokenRequestDTO(clientId, clientSecret);

        try {
            // Make the POST request to User Service's token endpoint
            ResponseEntity<LoginResponseDTO> response = restTemplate.postForEntity(tokenEndpoint, requestDTO, LoginResponseDTO.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                this.currentToken = response.getBody().getToken();
                // Extract expiration from the token itself or calculate from issue time + configured expiry
                // For simplicity, we'll re-use the JWT_EXPIRATION from User Service's property (hardcoded for now)
                // In a real scenario, you'd get expiry from the token's 'exp' claim.
                this.tokenExpirationTime = Instant.now().plusMillis(86400000); // Assuming 24 hours, or you could parse JWT
                System.out.println("DEBUG: Successfully fetched new token.");
            } else {
                throw new CustomException("Failed to get token from User Service. Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("ERROR: Could not fetch service token: " + e.getMessage());
            throw new CustomException("Failed to obtain service token from User Service: " + e.getMessage());
        }
    }
}
