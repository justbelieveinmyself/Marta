package com.justbelieveinmyself.marta.domain.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshResponseDto {
    private String refreshToken;
    private String accessToken;
    public static RefreshResponseDto of(String refreshToken, String accessToken){
        return new RefreshResponseDto(refreshToken, accessToken);
    }
}
