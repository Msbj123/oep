package com.example.demo.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.DTO.UserRegistrationDTO;
import com.example.demo.DTO.UserRequestDTO;
import com.example.demo.Exception.CustomException;
import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Config.JwtUtil;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
 
    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

   
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }



     //Retrieves all users from the database and converts them to UserRequestDTOs.
    @Override
    public List<UserRequestDTO> findAllUsers() {
        List<User> userList = userRepository.findAll();
        List<UserRequestDTO> dtoList = new ArrayList<>();

        for (User user : userList) {
            UserRequestDTO dto = requestToDTO(user);
            dtoList.add(dto);
        }

        return dtoList;
    }


     //Retrieves a user by their ID.
    @Override
    public UserRequestDTO getUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found"));
        return requestToDTO(user);
    }

    /**
     * Registers a new user after validating email uniqueness and password confirmation.
     * Assigns ADMIN role to the first user, STUDENT to others.
     */
    @Override
    public UserDTO registerUser(UserRegistrationDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new CustomException("Email already exists");
        }

        String password = dto.getPassword();
        String confirmPassword = dto.getConfirmPassword();

        if (password == null || confirmPassword == null || !password.equals(confirmPassword)) {
            throw new CustomException("Password and confirm password do not match");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Assign ADMIN role to the first user, STUDENT to others
        if (userRepository.count() == 0) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.STUDENT);
        }

        return mapToDTO(userRepository.save(user));
    }


    //Authenticates a user by email and password.
    @Override
    public UserDTO loginUser(LoginDTO dto) {
        // Authenticate the user using Spring Security's AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        // If authentication is successful, generate JWT
        if (authentication.isAuthenticated()) {
            User user = userRepository.findByEmail(dto.getEmail())
                    .orElseThrow(() -> new CustomException("User not found after authentication "));

            // Generate JWT token
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    user.getEmail(), user.getPassword(),
                    List.of(() -> "ROLE_" + user.getRole().name()) // Convert Role to GrantedAuthority
            );
            String token = jwtUtil.generateToken(userDetails);

            // Create a UserDTO with the token
            UserDTO userDTO = mapToDTO(user);
            userDTO.setToken(token); // Set the generated token
            return userDTO;
        } else {
            throw new CustomException("Invalid credentials");
        }
    }

    //Updates user details by ID.
    @Override
    public UserDTO updateUser(Integer userId, UserRegistrationDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found"));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); 

        return mapToDTO(userRepository.save(user));
    }

    /**
     * Assigns a new role to a user.
     * Only ADMIN can update
     */
   /* @Override
   
    public void assignRole(Long userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found"));
        user.setRole(role);
        userRepository.save(user);
    }
*/

    //Converts a User entity to a UserDTO.
    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole()); // Include role in UserDTO
        return dto;
    }


    //Converts a User entity to a UserRequestDTO.
    private UserRequestDTO requestToDTO(User user) {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setUserId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        // Password is intentionally excluded for security
        dto.setRole(user.getRole());
        return dto;
    }
}