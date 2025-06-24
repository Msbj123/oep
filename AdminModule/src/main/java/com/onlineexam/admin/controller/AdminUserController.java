package com.onlineexam.admin.controller;

import com.onlineexam.admin.client.UserFeignClient; // Import the Feign Client interface
import com.onlineexam.admin.dto.UserRoleUpdateDTO; // DTO for the role update request body
import com.onlineexam.admin.Exception.CustomException; // Your custom exception for consistent error handling
import jakarta.validation.Valid; // For JSR 303/349/380 Bean Validation
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Admin-specific user operations, primarily user role assignment.
 * This controller acts as an orchestrator or proxy, delegating actual user-related
 * operations to the dedicated User Service using FeignClient.
 */
@RestController // Marks this class as a Spring REST Controller
@RequestMapping("/api/admin/users") // Base path for admin-user-related endpoints.
public class AdminUserController {

    @Autowired // Injects the UserFeignClient, allowing this controller to make HTTP calls to the User Service.
    private UserFeignClient userFeignClient;

    /**
     * Handles PUT requests to assign a specific role to a user.
     * This endpoint is meant for administrators. It calls the User Service internally
     * to perform the actual role update via FeignClient.
     *
     * @param id The ID of the user whose role is to be updated, extracted from the path.
     * @param role The new role to assign (e.g., "ADMIN", "STUDENT", "EXAMINER").
     * This is received as a request parameter.
     * @return ResponseEntity with 200 OK if the role assignment is successful.
     * @throws CustomException if the role is blank, the user is not found in User Service,
     * or if other errors occur during communication with User Service.
     * These exceptions are handled by the GlobalExceptionHandler.
     */
    @PutMapping("/{id}/role") // Maps HTTP PUT requests to /api/admin/users/{id}/role
    public ResponseEntity<Void> assignRoleToUser(@PathVariable Integer id, @RequestParam String role) {
        // Basic validation for the role parameter. Further validation can occur in User Service.
        if (role == null || role.trim().isEmpty()) {
            throw new CustomException("Role cannot be empty.");
        }
        
        try {
            // Delegate the role assignment to the UserFeignClient.
            // Feign will handle the HTTP call to the User Service, including adding the token via interceptor.
            userFeignClient.assignRoleToUser(id, role);
            
            // Return 200 OK with no content to indicate successful operation.
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Catch any exceptions from Feign client (e.g., FeignClientException for HTTP errors)
            // You might want more specific error handling based on FeignClientException subtypes.
            throw new CustomException("Failed to assign role via User Service: " + e.getMessage());
        }
    }
}
