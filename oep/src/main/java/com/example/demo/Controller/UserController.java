package com.example.demo.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.DTO.UserRegistrationDTO;
import com.example.demo.DTO.UserRequestDTO;
import com.example.demo.Entity.Role;
import com.example.demo.Service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/examProtal/userModule")
public class UserController {
    
    private UserService userService;

 // Constructor-based dependency injection for UserService
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // EndPoint to get all Users
 	// Only ADMIN can view all users
    @GetMapping("users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserRequestDTO>> findAllUsers() {
        List<UserRequestDTO> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

 // EndPoint to get user by id
    @GetMapping("getById/{id}")
    public ResponseEntity<UserRequestDTO> getUserById(@PathVariable Integer id) {
        UserRequestDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

 // EndPoint to register new User
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerNewUser(@Valid @RequestBody UserRegistrationDTO dto) {
        UserDTO registeredUser = userService.registerUser(dto);
        return ResponseEntity.ok(registeredUser);
    }

 // EndPoint to login the existing user
    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@RequestBody @Valid LoginDTO dto) {
        UserDTO user = userService.loginUser(dto);
        return ResponseEntity.ok(user);
    }

  //EndPoint to update the user profile
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @RequestBody @Valid UserRegistrationDTO dto) {
        UserDTO updatedUser = userService.updateUser(id, dto);
        return ResponseEntity.ok(updatedUser);
    }

  //EndPoint to update the role of a user
  // Only ADMIN can assign roles
    /*
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> assignRoleToUser(@PathVariable Long id, @RequestParam Role role) {
        userService.assignRole(id, role);
        return ResponseEntity.ok("Role Updated Successfully");
    }
    */
}

