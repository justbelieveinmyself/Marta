package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.domain.dto.auth.JwtResponseDto;
import com.justbelieveinmyself.marta.domain.dto.auth.RefreshRequestDto;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.jwt.JwtUtils;
import com.justbelieveinmyself.marta.repositories.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Value("jwt.refreshToken.expiration")
    private Duration expiration;

    public String createToken(User user){
//        var refreshToken = refreshToken()
    }

    public RefreshRequestDto refreshToken(RefreshRequestDto refreshRequestDto){
        var tokenOptional = refreshTokenRepository.findRefreshTokenByToken(refreshRequestDto.getRefreshToken());

    }
}
