package com.justbelieveinmyself.marta.domain.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
@Schema(title = "RegisterDTO", description = "DTO for register new User")
@Data
@AllArgsConstructor
public class RegisterDto {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String passwordConfirm;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String postalCode;
    private String country;
}
