package com.justbelieveinmyself.marta.domain.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Schema(description = "DTO for jwtauth")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
    @NotBlank(message = "Please, enter your username!")
    private String username;
    @NotBlank(message = "Please, enter your password!")
    private String password;
}
