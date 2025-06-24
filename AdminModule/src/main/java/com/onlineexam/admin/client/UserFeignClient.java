package com.onlineexam.admin.client;


import com.onlineexam.admin.dto.UserRoleUpdateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign Client interface for interacting with the User Service.
 * This interface declaratively defines the HTTP endpoints to be called on the User Service.
 * The 'url' property points directly to the User Service's base URL.
 * 'configuration' refers to a class that can provide custom configurations (e.g., token interceptors).
 */
@FeignClient(name = "user-service", url = "${user-service.url}", configuration = UserFeignClientConfig.class)
public interface UserFeignClient {

    /**
     * Declares a PUT request to update a user's role in the User Service.
     * This method corresponds to the User Service's '/examProtal/userModule/{id}/role' endpoint.
     * The @PathVariable and @RequestParam annotations are used just like in a Spring MVC controller.
     *
     * @param id The ID of the user whose role is to be updated.
     * @param role The new role to assign (e.g., "ADMIN", "STUDENT", "EXAMINER").
     * @return ResponseEntity<Void> from the User Service.
     */
    @PutMapping("/examProtal/userModule/{id}/role")
    ResponseEntity<Void> assignRoleToUser(@PathVariable("id") Integer id, @RequestParam("role") String role);

    // You can add other methods here if the Admin Service needs to call other User Service endpoints, e.g.:
    // @GetMapping("/examProtal/userModule/{id}/profile")
    // UserResponseDTO getUserProfile(@PathVariable("id") Integer id);
}
