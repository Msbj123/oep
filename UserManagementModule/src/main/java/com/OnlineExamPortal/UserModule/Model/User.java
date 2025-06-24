package com.OnlineExamPortal.UserModule.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "users")
@Data
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @NotBlank(message="Name field must be filled")
    private String name;

    @NotBlank(message="Email should not be null")
    @Email(message="Email should be vaild")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message="Must set a password")
    @Size(min=8,message="Password should maintain minimum of 8 charaters")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

}
