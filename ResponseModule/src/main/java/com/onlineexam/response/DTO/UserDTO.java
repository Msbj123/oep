package com.onlineexam.response.DTO;


import com.onlineexam.response.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class UserDTO {
	    private Integer userId;
	    private String name;
	    private String email;
	    private Role role; 
}