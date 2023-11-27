package com.justbelieveinmyself.marta.domain.dto.auth;

import com.justbelieveinmyself.marta.domain.dto.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Schema(description = "DTO for response to client with token")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private String refreshToken;
    private UserDto user;
}
