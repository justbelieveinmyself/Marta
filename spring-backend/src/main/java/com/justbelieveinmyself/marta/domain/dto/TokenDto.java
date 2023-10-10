package com.justbelieveinmyself.marta.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Schema(title = "OAuthTokenDTO",description = "DTO for OAuth")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {
    String userId;
    String token;
}
