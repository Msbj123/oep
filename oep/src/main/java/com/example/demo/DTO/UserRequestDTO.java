package com.example.demo.DTO;

import com.example.demo.Entity.Role;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserRequestDTO {
	private Integer userId;
    private String name;
    private String email;
    private Role role;

}

