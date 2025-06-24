package com.onlineexam.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor
public class UserRoleUpdateDTO {
 @NotBlank(message = "Role cannot be blank")
 private String role; // Role can be "ADMIN", "STUDENT", "EXAMINER"
}
