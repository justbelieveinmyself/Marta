package com.justbelieveinmyself.marta.controllers;

import com.justbelieveinmyself.marta.domain.dto.JwtRequest;
import com.justbelieveinmyself.marta.domain.dto.RegUserDto;
import com.justbelieveinmyself.marta.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegUserDto regUserDto){
        return authService.createNewUser(regUserDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest jwtRequest){
        return authService.createAuthToken(jwtRequest);
    }
}
