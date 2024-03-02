package com.justbelieveinmyself.marta.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.justbelieveinmyself.marta.domain.dto.SellerDto;
import com.justbelieveinmyself.marta.domain.dto.auth.*;
import com.justbelieveinmyself.marta.services.AuthService;
import com.justbelieveinmyself.marta.services.RefreshTokenService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthService authService;
    @MockBean
    private RefreshTokenService refreshTokenService;

    AuthControllerTest() {
    }

    @Test
    void register_ReturnsValidResponseEntity() throws Exception {
        RegisterDto registerDto = new RegisterDto("Dmitry", "Salenko", "user", "123", "123", LocalDate.now(), "Male", "test@mail.ru", "+79788112224", "Kerm st. 12", null, "New York", "4242232", "USA", "West");

        when(authService.createNewUser(any(), any())).thenReturn(ResponseEntity.ok(new SellerDto(1L, "user", ZonedDateTime.now(), 100L)));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String registerDtoJson = objectMapper.writeValueAsString(registerDto);

        MockMultipartFile jsonPart = new MockMultipartFile("regUser", "", "application/json", registerDtoJson.getBytes());

        mockMvc.perform(
                        multipart("/api/v1/auth/register")
                                .file(new MockMultipartFile("file", "test-image.test", "image/jpeg", new byte[0]))
                                .file(jsonPart)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", Matchers.equalTo("user")))
                .andExpect(jsonPath("$.ratingCount", Matchers.equalTo(100)));
    }


    @Test
    void login() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsername("testUsername");
        loginRequestDto.setPassword("testPassword");

        LoginResponseDto loginResponseDto = new LoginResponseDto("refreshToken", null);

        when(authService.createAuthToken(any())).thenReturn(ResponseEntity.ok(loginResponseDto));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"));
    }

    @Test
    void refreshToken() {
        RefreshRequestDto refreshRequestDto = new RefreshRequestDto("mytoken");
        RefreshResponseDto refreshResponseDto = new RefreshResponseDto("mytoken", "accesstoken", Instant.now(), Instant.now());
        when(refreshTokenService.refreshToken(any())).thenReturn(refreshResponseDto);

        RefreshResponseDto refreshResponseDtoTest = refreshTokenService.refreshToken(refreshRequestDto);

        Assertions.assertEquals(refreshResponseDto, refreshResponseDtoTest);
    }
}