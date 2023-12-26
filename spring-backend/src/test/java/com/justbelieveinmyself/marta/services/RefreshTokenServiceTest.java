package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.domain.dto.auth.RefreshRequestDto;
import com.justbelieveinmyself.marta.domain.dto.auth.RefreshResponseDto;
import com.justbelieveinmyself.marta.domain.entities.RefreshToken;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.exceptions.RefreshTokenException;
import com.justbelieveinmyself.marta.repositories.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {"jwt.refreshToken.expiration=50m"})
class RefreshTokenServiceTest {
    @Autowired
    private RefreshTokenService refreshTokenService;
    @MockBean
    private RefreshTokenRepository refreshTokenRepository;
    @Test
    void createRefreshToken_whenRefreshTokenIsNull() {
        User mockUser = User.builder().id(1L).refreshToken(null).build();

        when(refreshTokenRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        String refreshToken = refreshTokenService.createRefreshToken(mockUser);

        assertNotNull(refreshToken);

        verify(refreshTokenRepository, times(1)).save(any());
        verify(refreshTokenRepository, times(0)).delete(any());
    }

    @Test
    void createRefreshToken_whenRefreshTokenIsNotNull() {
        User mockUser = User.builder().id(1L).build();
        RefreshToken mockToken = new RefreshToken(1L, UUID.randomUUID().toString(), "token123", ZonedDateTime.now(), mockUser);
        mockUser.setRefreshToken(mockToken);

        when(refreshTokenRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        String refreshToken = refreshTokenService.createRefreshToken(mockUser);

        assertNotNull(refreshToken);

        verify(refreshTokenRepository, times(1)).save(any());
        verify(refreshTokenRepository, times(1)).delete(any());
    }

    @Test
    void refreshToken_whenRefreshTokenValid() {
        User mockUser = User.builder().id(1L).build();
        RefreshToken mockRefreshToken = new RefreshToken(1L, UUID.randomUUID().toString(), "token12345", ZonedDateTime.now().plusMinutes(30L), mockUser);
        RefreshRequestDto refreshRequestDto = new RefreshRequestDto("token12345");

        when(refreshTokenRepository.findRefreshTokenByToken("token12345")).thenReturn(Optional.of(mockRefreshToken));

        RefreshResponseDto refreshResponseDto = refreshTokenService.refreshToken(refreshRequestDto);

        verify(refreshTokenRepository, times(0)).delete(any());
        verify(refreshTokenRepository, times(1)).findRefreshTokenByToken("token12345");
    }

    @Test
    void refreshToken_whenRefreshTokenNotExist() {
        User mockUser = User.builder().id(1L).build();
        RefreshRequestDto refreshRequestDto = new RefreshRequestDto("token12345");

        when(refreshTokenRepository.findRefreshTokenByToken("token12345")).thenReturn(Optional.ofNullable(null));

        assertThrows(RefreshTokenException.class, () -> {
            RefreshResponseDto refreshResponseDto = refreshTokenService.refreshToken(refreshRequestDto);
        });

        verify(refreshTokenRepository, times(0)).delete(any());
        verify(refreshTokenRepository, times(1)).findRefreshTokenByToken("token12345");
    }

    @Test
    void refreshToken_whenRefreshTokenExpired() {
        User mockUser = User.builder().id(1L).build();
        RefreshToken mockRefreshToken = new RefreshToken(1L, UUID.randomUUID().toString(), "token12345", ZonedDateTime.now().minusMinutes(30L), mockUser);
        RefreshRequestDto refreshRequestDto = new RefreshRequestDto("token12345");

        when(refreshTokenRepository.findRefreshTokenByToken("token12345")).thenReturn(Optional.of(mockRefreshToken));

        assertThrows(RefreshTokenException.class, () -> {
            RefreshResponseDto refreshResponseDto = refreshTokenService.refreshToken(refreshRequestDto);
        });

        verify(refreshTokenRepository, times(1)).delete(any());
        verify(refreshTokenRepository, times(1)).findRefreshTokenByToken("token12345");
    }
}