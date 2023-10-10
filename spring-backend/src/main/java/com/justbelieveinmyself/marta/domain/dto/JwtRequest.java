package com.justbelieveinmyself.marta.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
@Schema(description = "DTO for jwtauth")
@Getter
@Setter
public class JwtRequest {
    private String username;
    private String password;
}
