package com.justbelieveinmyself.marta.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Information about User")
public class UserDto {
    private Long id;
    private String username;
    private String email;
}
