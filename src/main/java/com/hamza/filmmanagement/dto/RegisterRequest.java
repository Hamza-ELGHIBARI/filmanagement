package com.hamza.filmmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RegisterRequest {
    @Email
    @NotBlank
    private String email;
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password;
    private Set<String> roles;
}
