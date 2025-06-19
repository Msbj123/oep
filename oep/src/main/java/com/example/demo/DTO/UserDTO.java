package com.example.demo.DTO;

import com.example.demo.Entity.Role; // Import Role enum

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserDTO {
	    private Integer userId;
	    private String name;
	    private String email;
	    private Role role; 
	    private String token;
}