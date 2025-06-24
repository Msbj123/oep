package com.onlineexam.admin.client;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.onlineexam.admin.Exception.CustomException; // Import CustomException

/**
 * Feign Client configuration for the User Service client.
 * This class provides a RequestInterceptor that dynamically adds the
 * Authorization header with a valid JWT token obtained from the TokenProvider.
 */
@Configuration
public class UserFeignClientConfig {

    /**
     * Defines a RequestInterceptor bean for Feign.
     * This interceptor will add the Authorization header with the Bearer token
     * to all requests made through the UserFeignClient.
     * It uses TokenProvider to get a fresh or existing valid token.
     *
     * @param tokenProvider Automatically injected instance of TokenProvider.
     * @return A RequestInterceptor instance.
     */
    @Bean
    public RequestInterceptor requestInterceptor(TokenProvider tokenProvider) {
        return requestTemplate -> {
            String dynamicToken = null;
            try {
                // Attempt to get a valid token from the TokenProvider
                dynamicToken = tokenProvider.getValidToken();
            } catch (CustomException e) {
                // Log the custom exception if token generation fails.
                // This prevents the application from crashing at startup if the User Service is down
                // or returns an error during token request.
                System.err.println("ERROR: Failed to get dynamic token for Feign client: " + e.getMessage());
                // You might choose to set a default error header or rethrow a specific type of exception
                // that your global exception handler can catch for Feign client errors.
                // For now, the request will proceed without the Authorization header, likely failing
                // at the User Service with a 401 Unauthorized or 403 Forbidden.
            } catch (Exception e) {
                // Catch any other unexpected exceptions during token retrieval
                System.err.println("ERROR: An unexpected error occurred while getting token for Feign client: " + e.getMessage());
            }

            if (dynamicToken != null && !dynamicToken.isEmpty()) {
                requestTemplate.header("Authorization", "Bearer " + dynamicToken);
            } else {
                System.err.println("Warning: Dynamic token is null or empty after retrieval attempt. Request might fail if authentication is required.");
            }
        };
    }
}
