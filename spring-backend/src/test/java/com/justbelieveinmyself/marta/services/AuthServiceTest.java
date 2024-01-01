package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.domain.dto.SellerDto;
import com.justbelieveinmyself.marta.domain.dto.auth.LoginRequestDto;
import com.justbelieveinmyself.marta.domain.dto.auth.LoginResponseDto;
import com.justbelieveinmyself.marta.domain.dto.auth.RegisterDto;
import com.justbelieveinmyself.marta.domain.entities.RefreshToken;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.exceptions.NotCreatedException;
import com.justbelieveinmyself.marta.repositories.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.TestPropertySource;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {"jwt.refreshToken.expiration=50m"})
class AuthServiceTest {

    @MockBean
    private UserService userService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private AuthService authService;
    @Test
    void createAuthToken_shouldCreateAndReturnToken() {
        User mockUser = User.builder().id(1L).username("user").password("123").build();
        LoginRequestDto loginRequestDto = new LoginRequestDto("user", "123");

        when(refreshTokenRepository.save(any())).thenReturn(new RefreshToken(1L, UUID.randomUUID().toString(), "myrefreshtoken", ZonedDateTime.now(), mockUser));
        when(userService.loadUserByUsername("user")).thenReturn(mockUser);

        ResponseEntity<LoginResponseDto> authToken = authService.createAuthToken(loginRequestDto);
        assertEquals(mockUser.getUsername(), authToken.getBody().getUser().getUsername());
        assertEquals("myrefreshtoken", authToken.getBody().getRefreshToken());
    }

    @Test
    void createNewUser_shouldCreateNewUser_whenValidRegisterDtoAndUserNotExists() {
        User mockUser = User.builder().id(1L).username("user").email("test@mail.ru").build();
        RegisterDto registerDto = new RegisterDto("Dmitry","Salenko",  "user", "123", "123", "test@mail.ru", "+79788112224", "Kerm st. 12", "New York", "4242232", "USA");

        when(userService.createNewUser(any(), any())).thenReturn(mockUser);

        ResponseEntity<SellerDto> test = authService.createNewUser(registerDto, new MockMultipartFile("test", "test".getBytes()));
        assertEquals("user", test.getBody().getUsername());
        assertEquals("test@mail.ru", test.getBody().getEmail());
    }

    @Test
    void createNewUser_shouldThrowException_whenInvalidRegisterDto() {
        User mockUser = User.builder().id(1L).username("user").email("test@mail.ru").build();
        RegisterDto registerDto = new RegisterDto("Dmitry","Salenko",  "user", "321", "123", "test@mail.ru", "+79788112224", "Kerm st. 12", "New York", "4242232", "USA");

        when(userService.createNewUser(any(), any())).thenReturn(mockUser);

        assertThrows(NotCreatedException.class, () -> {
            ResponseEntity<SellerDto> test = authService.createNewUser(registerDto, new MockMultipartFile("test", "test".getBytes()));
        });
    }

    @Test
    void createNewUser_shouldThrowException_whenUserExists() {
        User mockUser = User.builder().id(1L).username("user").email("test@mail.ru").build();
        RegisterDto registerDto = new RegisterDto("Dmitry","Salenko",  "user", "321", "123", "test@mail.ru", "+79788112224", "Kerm st. 12", "New York", "4242232", "USA");

        when(userService.findByUsername("user")).thenReturn(mockUser);

        assertThrows(NotCreatedException.class, () -> {
            ResponseEntity<SellerDto> test = authService.createNewUser(registerDto, new MockMultipartFile("test", "test".getBytes()));
        });
    }
}