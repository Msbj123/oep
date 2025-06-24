/*package com.OnlineExamProtal.UserModule.ControllerTest;

import com.OnlineExamPortal.UserModule.Controller.UserController;
import com.OnlineExamPortal.UserModule.DTO.*;
import com.OnlineExamPortal.UserModule.Model.Role;
import com.OnlineExamPortal.UserModule.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testFindAllUsers() throws Exception {
        List<UserRequestDTO> users = Arrays.asList(new UserRequestDTO(), new UserRequestDTO());
        when(userService.findAllUsers()).thenReturn(users);

        mockMvc.perform(get("/examProtal/userModule/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(users)));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "STUDENT"})
    public void testGetUserById() throws Exception {
        UserRequestDTO user = new UserRequestDTO();
        when(userService.getUserById((int) 1L)).thenReturn(user);

        mockMvc.perform(get("/examProtal/userModule/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    public void testRegisterNewUser() throws Exception {
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        UserDTO userDTO = new UserDTO();
        when(userService.registerUser(any(UserRegistrationDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/examProtal/userModule/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDTO)));
    }

    @Test
    public void testLoginUser() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        UserDTO userDTO = new UserDTO();
        when(userService.loginUser(any(LoginDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/examProtal/userModule/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDTO)));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "STUDENT"})
    public void testUpdateUser() throws Exception {
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        UserDTO userDTO = new UserDTO();
        when(userService.updateUser(any(Integer.class), any(UserRegistrationDTO.class))).thenReturn(userDTO);

        mockMvc.perform(put("/examProtal/userModule/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDTO)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testAssignRoleToUser() throws Exception {
        mockMvc.perform(put("/examProtal/userModule/1/role")
                .param("role", Role.ADMIN.name()))
                .andExpect(status().isOk());
    }
}
*/