package com.onlineexam.response.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.onlineexam.response.DTO.UserDTO;
//import com.onlineexam.response.DTO.UserDTO;
//import com.onlineexam.response.DTO.UserResponseDTO;
import com.onlineexam.response.DTO.UserResponseDTO;

/**
 * Feign Client for Exam Management Service to communicate with User Service.
 * Used to fetch user details (e.g., name, email for reports).
 */
@FeignClient(name = "user-service", url = "${user-service.url}")
public interface UserFeignClient {

    /**
     * Retrieves a user's profile by their ID from the User Service.
     * @param id The ID of the user.
     * @return ResponseEntity containing UserResponseDTO if found.
     */
    @GetMapping("/examProtal/userModule/{id}/profile") // Ensure this path matches the User Service's UserController
    ResponseEntity<UserDTO> getUserProfile(@PathVariable("id") Integer id);
    

    
}
