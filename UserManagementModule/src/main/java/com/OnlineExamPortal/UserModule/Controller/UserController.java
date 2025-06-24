
package com.OnlineExamPortal.UserModule.Controller;

import com.OnlineExamPortal.UserModule.DTO.LoginDTO;
import com.OnlineExamPortal.UserModule.DTO.UserDTO;
import com.OnlineExamPortal.UserModule.DTO.UserRegistrationDTO;
import com.OnlineExamPortal.UserModule.DTO.UserRequestDTO;
//import com.OnlineExamPortal.UserModule.DTO.UserResponseDTO;
import com.OnlineExamPortal.UserModule.Exception.CustomException;
import com.OnlineExamPortal.UserModule.Model.Role;
import com.OnlineExamPortal.UserModule.Service.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for managing User resources.
 * Provides endpoints for user registration, login, profile management, and role assignment.
 */

@RestController // Marks this class as a Spring REST Controller
@RequestMapping("/examProtal/userModule") // Base path for all endpoints in this controller
public class UserController {

    private final UserService userService;

    // Constructor-based dependency injection for UserService
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Handles POST requests for user registration.
     * @param registrationDTO The DTO containing user registration details.
     * @return ResponseEntity with the registered user's details and 200 OK status.
     */
    
    @PostMapping("/register") // Maps HTTP POST requests to /examProtal/userModule/register
    public ResponseEntity<UserDTO> registerNewUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        UserDTO registeredUser = userService.registerUser(registrationDTO);
        return ResponseEntity.ok(registeredUser);
    }

    /**
     * Handles POST requests for user login.
     * @param loginDTO The DTO containing user login credentials (email and password).
     * @return ResponseEntity with a JWT token upon successful login and 200 OK status.
     * @throws CustomException if authentication fails (e.g., invalid credentials).
 
     */
    
    @PostMapping("/login") // Maps HTTP POST requests to /examProtal/userModule/login
    public ResponseEntity<UserDTO> loginUser(@RequestBody @Valid LoginDTO loginDTO) {
        UserDTO user = userService.loginUser(loginDTO);
        return ResponseEntity.ok(user);
    }

    /**
     * EndPoint to get all Users
     * Only ADMIN can view all users
     * @return ResponseEntity with a list of UserRequestDTOs.
     */
    
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserRequestDTO>> findAllUsers() {
        List<UserRequestDTO> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * EndPoint to get user by id
     * @param id The ID of the user whose profile is to be retrieved.
     * @return ResponseEntity with the user's profile details if found (200 OK), or 404 Not Found if not.
     */
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public ResponseEntity<UserRequestDTO> getUserById(@PathVariable Integer id) {
        UserRequestDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    

    /**
     * EndPoint to update the user profile
     * @param id The ID of the user to update.
     * @param dto The DTO containing the updated user details.
     * @return ResponseEntity with the updated user's details and 200 OK status.
     */
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @RequestBody @Valid UserRegistrationDTO dto) {
        UserDTO updatedUser = userService.updateUser(id, dto);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * EndPoint to update the role of a user
     * Only ADMIN can assign roles
     * @param id The ID of the user whose role is to be updated.
     * @param role The new role to assign (as a request parameter).
     * @return ResponseEntity with a success message and 200 OK status.
     * @throws CustomException if the user is not found or the role is invalid.
     */
    
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> assignRoleToUser(@PathVariable Integer id, @RequestParam Role role) {
        userService.assignRole(id, role);
        return ResponseEntity.ok("Role Updated Successfully");
    }
    /*
    @GetMapping("/{id}/profile")
    public ResponseEntity<UserResponseDTO> getUserProfile(@PathVariable("id") Integer id) {
        UserResponseDTO userProfile = userService.getUserProfileById(id);
        if (userProfile == null) {
            throw new CustomException("User not found with ID: " + id);
        }
        return ResponseEntity.ok(userProfile);
    }
    */
    @GetMapping("/{id}/profile")
    public UserRequestDTO getUserProfileById(@PathVariable Integer id) {
    	return userService.getUserProfileById(id);
    }
}
