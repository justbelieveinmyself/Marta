package com.justbelieveinmyself.marta.domain.dto.auth;

import com.justbelieveinmyself.marta.domain.dto.UserDto;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.Set;
@Schema(description = "DTO for response to client with token")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private String refreshToken;
    private UserDto user;
}
