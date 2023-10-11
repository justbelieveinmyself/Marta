package com.justbelieveinmyself.marta.controllers;

import com.justbelieveinmyself.marta.domain.dto.auth.JwtRequestDto;
import com.justbelieveinmyself.marta.domain.dto.auth.RegisterDto;
import com.justbelieveinmyself.marta.exceptions.ResponseError;
import com.justbelieveinmyself.marta.services.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@Tag(
        name = "Auth",
        description = "The Auth API"
)
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(value = "/register", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> register(@RequestPart("regUser") RegisterDto registerDTO,
                                      @RequestPart(name = "file",required = false) MultipartFile file){
        try {
            return authService.createNewUser(registerDTO, file);
        } catch (IOException e) {
            System.out.println("Failed to save profile image!");
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ResponseError(HttpStatus.NOT_FOUND.value(), "User not created, cannot save avatar file")
                    , HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequestDto jwtRequestDTO){
        return authService.createAuthToken(jwtRequestDTO);
    }
}
