package com.OnlineExamPortal.UserModule.DTO;

import com.OnlineExamPortal.UserModule.Model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
	private Integer userId;
    private String name;
    private String email;
    private Role role;

}

