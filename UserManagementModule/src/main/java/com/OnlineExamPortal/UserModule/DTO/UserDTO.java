package com.OnlineExamPortal.UserModule.DTO;

import com.OnlineExamPortal.UserModule.Model.Role;

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