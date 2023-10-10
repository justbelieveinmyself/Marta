package com.justbelieveinmyself.marta.domain.dto.auth;

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
public class OAuthTokenDto {
    String userId;
    String token;
}
