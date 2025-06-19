package com.example.demo.Service;

import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.DTO.UserRegistrationDTO;
import com.example.demo.DTO.UserRequestDTO;
import com.example.demo.Entity.Role;

import java.util.List;

public interface UserService {
    List<UserRequestDTO> findAllUsers();
    UserRequestDTO getUserById(Integer userId);
    UserDTO registerUser(UserRegistrationDTO dto);
    UserDTO loginUser(LoginDTO dto);
    UserDTO updateUser(Integer userId, UserRegistrationDTO dto);
    //void assignRole(Long userId, Role role);
}