package com.justbelieveinmyself.marta.domain.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshResponseDto {
    private String refreshToken;
    private String accessToken;
    private Instant refreshTokenExpiration;
    private Instant accessTokenExpiration;
}
