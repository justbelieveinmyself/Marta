package com.justbelieveinmyself.marta.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justbelieveinmyself.marta.exceptions.ResponseError;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Autowired @Lazy
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        String errorMessage = "";
        HttpStatus httpStatus = null;
        if(jwtAuthorizationFilter.getAuthentication(request) != null){
            errorMessage = "Bad request!";
            httpStatus = HttpStatus.BAD_REQUEST;
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }else{
            errorMessage = "Bad access token!";
            httpStatus = HttpStatus.UNAUTHORIZED;
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        response.getWriter().write(new ObjectMapper().writeValueAsString(new ResponseError(
                httpStatus,
                errorMessage)));
    }
}
