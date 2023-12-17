package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import com.justbelieveinmyself.marta.domain.dto.SellerDto;
import com.justbelieveinmyself.marta.domain.dto.auth.RegisterDto;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.mappers.ProductMapper;
import com.justbelieveinmyself.marta.domain.mappers.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class AuthServiceTest {

    @MockBean
    private UserService userService;
    @MockBean
    private ProductMapper productMapper;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private RefreshTokenService refreshTokenService;
    @MockBean
    private UserMapper userMapper;
    @MockBean
    private FileHelper fileHelper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthService authService;
    @Test
    void createAuthToken() {

    }

    @Test
    void createNewUser() {
        User mockUser = User.builder().id(1L).username("user").email("test@mail.ru").build();
        RegisterDto registerDto = new RegisterDto("Dmitry","Salenko",  "user", "123", "123", "test@mail.ru", "+79788112224", "Kerm st. 12", "New York", "4242232", "USA");

        when(userService.createNewUser(any(), any())).thenReturn(mockUser);

        ResponseEntity<SellerDto> test = authService.createNewUser(registerDto, new MockMultipartFile("test", "test".getBytes()));
        assertEquals("user", test.getBody().getUsername());
        assertEquals("test@mail.ru", test.getBody().getEmail());

    }
}