package com.justbelieveinmyself.marta.domain.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(title = "RegisterDTO", description = "DTO for register new User")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterDto {
    @NotBlank(message = "Please, enter your firstName!")
    private String firstName;
    @NotBlank(message = "Please, enter your lastName!")
    private String lastName;
    @NotBlank(message = "Please, enter your username!")
    private String username;
    @NotBlank(message = "Please, enter your password!")
    private String password;
    @NotBlank(message = "Please, enter confirm password!")
    private String passwordConfirm;
    @NotBlank(message = "Please, enter your birthDate! ex. 11-05-2004")
    private LocalDate birthDate;
    @NotBlank(message = "Please, enter your gender! ex. MALE (FEMALE)")
    private String gender;
    @NotBlank(message = "Please, enter your email!")
    @Email(message = "Email is invalid!")
    private String email;
    @NotBlank(message = "Please, enter your phone!")
    private String phone;
    @NotBlank(message = "Please, enter your address!")
    private String address;
    private String address2;
    @NotBlank(message = "Please, enter your city!")
    private String city;
    @NotBlank(message = "Please, enter your postalCode!")
    private String postalCode;
    @NotBlank(message = "Please, enter your country!")
    private String country;
    @NotBlank(message = "Please, enter your region!")
    @NotNull
    private String region;
}
