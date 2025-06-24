package com.OnlineExamPortal.UserModule.Service;

import com.OnlineExamPortal.UserModule.DTO.LoginDTO;
import com.OnlineExamPortal.UserModule.DTO.ServiceTokenRequestDTO;
import com.OnlineExamPortal.UserModule.DTO.UserDTO;
import com.OnlineExamPortal.UserModule.DTO.UserRegistrationDTO;
import com.OnlineExamPortal.UserModule.DTO.UserRequestDTO;
//import com.OnlineExamPortal.UserModule.DTO.UserResponseDTO;
import com.OnlineExamPortal.UserModule.Exception.CustomException;
import com.OnlineExamPortal.UserModule.Model.Role;
import com.OnlineExamPortal.UserModule.Model.User;
import com.OnlineExamPortal.UserModule.Repository.UserRepository;
import com.OnlineExamPortal.UserModule.config.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the UserService interface.
 * Contains the business logic for user management, authentication, and authorization.
 */

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Registers a new user.
     * - Validates if a user with the given email already exists.
     * - Hashes the password using BCrypt.
     * - Assigns ADMIN role to the first user, STUDENT to others.
     * @param registrationDTO Data for the new user.
     * @return UserDTO of the registered user.
     * @throws CustomException if a user with the email already exists or passwords don't match.
     */
    
    @Transactional
    @Override
    public UserDTO registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new CustomException("Email already exists");
        }

        String password = registrationDTO.getPassword();
        String confirmPassword = registrationDTO.getConfirmPassword();

        if (password == null || confirmPassword == null || !password.equals(confirmPassword)) {
            throw new CustomException("Password and confirm password do not match");
        }

        User user = new User();
        user.setName(registrationDTO.getName());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        // Assign ADMIN role to the first user, STUDENT to others
        if (userRepository.count() == 0) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.STUDENT);
        }

        return mapToDTO(userRepository.save(user));
    }

    /**
     * Authenticates a user based on provided credentials.
     * - Uses Spring Security's AuthenticationManager to authenticate.
     * - If authentication is successful, generates a JWT token.
     * @param loginDTO User's login credentials.
     * @return UserDTO containing the JWT token and user's email.
     * @throws CustomException if authentication fails (e.g., bad credentials).
     */
    
    @Transactional(readOnly = true)
    @Override
    public UserDTO loginUser(LoginDTO loginDTO) {
        try {
            // Authenticate the user using Spring Security's AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
            );

            // If authentication is successful, get the authenticated User
            if (authentication.isAuthenticated()) {
                User user = userRepository.findByEmail(loginDTO.getEmail())
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

        } catch (AuthenticationException e) {
            // Catch authentication specific exceptions (e.g., BadCredentialsException)
            throw new CustomException("Invalid email or password.");
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            System.err.println("Error during login: " + e.getMessage());
            throw new CustomException("An unexpected error occurred during login.");
        }
    }

    /**
     * Retrieves all users from the database and converts them to UserRequestDTOs.
     * @return A list of UserRequestDTOs.
     */

    @Transactional(readOnly = true)
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

    /**
     * Retrieves a user by their ID.
     * @param userId The ID of the user to retrieve.
     * @return UserRequestDTO of the found user.
     * @throws CustomException if the user is not found.
     */
    
    @Transactional(readOnly = true)
    @Override
    public UserRequestDTO getUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found"));
        return requestToDTO(user);
    }

    /**
     * Updates user details by ID.
     * @param userId The ID of the user to update.
     * @param dto The DTO containing the updated user details.
     * @return UserDTO of the updated user.
     * @throws CustomException if the user is not found.
     */
    
    @Transactional
    @Override
    public UserDTO updateUser(Integer userId, UserRegistrationDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found"));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        // Only encode if password is provided and needs to be updated
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return mapToDTO(userRepository.save(user));
    }
	
    /**
     * Assigns a new role to a user. This operation is typically restricted to ADMIN users.
     * @param userId The ID of the user whose role is to be updated.
     * @param role The new role to assign.
     * @throws CustomException if the user is not found.
     */
    
    @Transactional
    @Override
    public void assignRole(Integer userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found"));

        // Update the user's role
        user.setRole(role);
        userRepository.save(user); // Save the updated user
    }

    /**
     * Generates a service-to-service token for internal clients (like admin-service).
     * This method authenticates the client using a predefined client ID and secret.
     * If valid, it generates an ADMIN-level JWT token.
     * @param requestDTO Contains the client ID and client secret.
     * @return UserDTO containing the service token.
     * @throws CustomException if client credentials are invalid.
     */
    
    @Override
    public UserDTO generateServiceToken(ServiceTokenRequestDTO requestDTO) {
        // Hardcoded service client credentials for simplicity. In production, manage these securely.
        final String ADMIN_SERVICE_CLIENT_ID = "admin-service-client";
        final String ADMIN_SERVICE_CLIENT_SECRET = "superSecretAdminServicePassword123";

        // Validate client credentials
        if (!ADMIN_SERVICE_CLIENT_ID.equals(requestDTO.getClientId()) ||
            !ADMIN_SERVICE_CLIENT_SECRET.equals(requestDTO.getClientSecret())) {
            throw new CustomException("Invalid client credentials for service token generation.");
        }

        // For service tokens, we can create a dummy UserDetails object for token generation
        // Or fetch a specific "service account" user from DB if it exists.
        // For simplicity, let's create a temporary User object with ADMIN role for token generation.
        User serviceAccountUser = new User();
        serviceAccountUser.setEmail(ADMIN_SERVICE_CLIENT_ID + "@service.com"); // Use client ID as email for subject
        serviceAccountUser.setRole(Role.ADMIN); // Ensure service token has ADMIN role

        // Generate JWT token
        String token = jwtUtil.generateToken(
                new org.springframework.security.core.userdetails.User(
                        serviceAccountUser.getEmail(), "", // Password not needed for service account UserDetails
                        List.of(() -> "ROLE_" + serviceAccountUser.getRole().name())
                )
        );

        UserDTO userDTO = new UserDTO();
        userDTO.setToken(token);
        userDTO.setEmail(serviceAccountUser.getEmail());
        userDTO.setRole(serviceAccountUser.getRole());
        return userDTO;
    }


    /**
     * Helper method to convert a User entity to a UserDTO.
     * This ensures sensitive data (like hashed password) is not exposed in responses.
     * @param user The User entity to convert.
     * @return The corresponding UserDTO.
     */
    
    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole()); // Include role in UserDTO
        return dto;
    }
	
    /**
     * Helper method to convert a User entity to a UserRequestDTO.
     * @param user The User entity to convert.
     * @return The corresponding UserRequestDTO.
     */
   
    private UserRequestDTO requestToDTO(User user) {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setUserId(user.getUserId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        // Password is intentionally excluded for security
        dto.setRole(user.getRole());
        return dto;
    }
    
    
    @Override
    public UserRequestDTO getUserProfileById(Integer id)
    {
    	Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return null;
        }

        User user = userOptional.get();
        // Map User entity to UserResponseDTO
        return new UserRequestDTO(
        		user.getUserId(),
        		user.getName(),
            user.getEmail(),
            user.getRole()
        		);
    }
    
    
}
    