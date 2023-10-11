package com.justbelieveinmyself.marta.domain.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshResponseDto {
    private String refreshToken;
    private String accessToken;
}
