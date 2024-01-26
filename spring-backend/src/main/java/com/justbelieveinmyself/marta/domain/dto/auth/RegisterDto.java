package com.justbelieveinmyself.marta.domain.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(title = "RegisterDTO", description = "DTO for register new User")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String passwordConfirm;
    private LocalDate birthDate;
    private String gender;
    private String email;
    private String phone;
    private String address;
    private String address2;
    private String city;
    private String postalCode;
    private String country;
    private String region;
}
