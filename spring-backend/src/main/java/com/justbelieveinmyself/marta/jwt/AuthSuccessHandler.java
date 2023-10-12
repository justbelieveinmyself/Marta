package com.justbelieveinmyself.marta.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justbelieveinmyself.marta.domain.dto.auth.JwtResponseDto;
import com.justbelieveinmyself.marta.domain.dto.auth.RefreshResponseDto;
import com.justbelieveinmyself.marta.services.RefreshTokenService;
import com.justbelieveinmyself.marta.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
@Slf4j
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private UserService userService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private JwtUtils jwtUtils;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        var user = userService.loadUserByUsername(principal.getUsername());
        String token = jwtUtils.createJwt(user.getUsername());
        String refreshToken = refreshTokenService.createToken(user);
        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("Content-Type", "application/json");
        logger.debug("token %s".formatted(token));
        response.getWriter().write(objectMapper.writeValueAsString(RefreshResponseDto.of(refreshToken, token)));
    }
}
