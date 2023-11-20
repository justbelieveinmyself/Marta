package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.domain.dto.auth.RefreshRequestDto;
import com.justbelieveinmyself.marta.domain.dto.auth.RefreshResponseDto;
import com.justbelieveinmyself.marta.domain.entities.RefreshToken;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.exceptions.RefreshTokenException;
import com.justbelieveinmyself.marta.jwt.JwtUtils;
import com.justbelieveinmyself.marta.repositories.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${jwt.refreshToken.expiration}")
    private Duration expiration;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtUtils jwtUtils) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtils = jwtUtils;
    }

    public String createToken(User user){
        if(user.getRefreshToken() != null) {
            refreshTokenRepository.delete(user.getRefreshToken());
            user.setRefreshToken(null);
        }
        var refreshToken = refreshTokenRepository.save(RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiration(ZonedDateTime.now(ZoneId.systemDefault()).plusMinutes(expiration.toMinutes()))
                .build());
        return refreshToken.getToken();
    }

    public RefreshResponseDto refreshToken(RefreshRequestDto refreshRequestDto){
        var tokenOptional = refreshTokenRepository.findRefreshTokenByToken(refreshRequestDto.getRefreshToken());
        if(tokenOptional.isEmpty()){
            throw new RefreshTokenException("Refresh token %s not found".formatted(refreshRequestDto.getRefreshToken()));
        }
        var token = tokenOptional.get();
        if(isTokenExpired(token.getExpiration())){
            refreshTokenRepository.delete(token);
            throw new RefreshTokenException("Refresh token %s is expired".formatted(refreshRequestDto.getRefreshToken()));
        }
        String jwt = jwtUtils.createAccessToken(token.getUser().getUsername());
        updateToken(token);
        return RefreshResponseDto.of(token.getToken(), jwt);
    }

    private void updateToken(RefreshToken token) {
        token.setExpiration(ZonedDateTime.now(ZoneId.systemDefault()).plusMinutes(expiration.toMinutes()));
        refreshTokenRepository.save(token);
    }

    private boolean isTokenExpired(ZonedDateTime expirationTime){
        return expirationTime.isBefore(ZonedDateTime.now(ZoneId.systemDefault()));
    }
}
