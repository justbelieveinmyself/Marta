package com.justbelieveinmyself.marta.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justbelieveinmyself.marta.domain.dto.SellerDto;
import com.justbelieveinmyself.marta.domain.dto.auth.LoginRequestDto;
import com.justbelieveinmyself.marta.domain.dto.auth.LoginResponseDto;
import com.justbelieveinmyself.marta.domain.dto.auth.RegisterDto;
import com.justbelieveinmyself.marta.services.AuthService;
import com.justbelieveinmyself.marta.services.RefreshTokenService;
import com.justbelieveinmyself.marta.services.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private AuthService authService;
    @MockBean
    private RefreshTokenService refreshTokenService;

    @Test
    void register_ReturnsValidResponseEntity() throws Exception {
        RegisterDto registerDto = new RegisterDto("Dmitry", "Salenko", "user", "123", "123", "test@mail.ru", "+79788112224", "Kerm st. 12", "New York", "4242232", "USA");


        when(authService.createNewUser(any(), any())).thenReturn(ResponseEntity.ok(new SellerDto(1L, "user", "test@mail.ru")));

        ObjectMapper objectMapper = new ObjectMapper();
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
                .andExpect(jsonPath("$.email", Matchers.equalTo("test@mail.ru")));
    }


    @Test
    void login() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsername("testUsername");
        loginRequestDto.setPassword("testPassword");

        LoginResponseDto loginResponseDto = new LoginResponseDto("refreshToken", null);

        when(authService.createAuthToken(any())).thenReturn(ResponseEntity.ok(loginResponseDto));

        // Выполняем запрос и проверяем, что получаем ожидаемый JSON-ответ
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"));
    }

    @Test
    void refreshToken() {
    }
}