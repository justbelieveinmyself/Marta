package com.justbelieveinmyself.marta.controllers;

import com.justbelieveinmyself.marta.domain.dto.JwtRequest;
import com.justbelieveinmyself.marta.domain.dto.RegUserDto;
import com.justbelieveinmyself.marta.exceptions.AppError;
import com.justbelieveinmyself.marta.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(value = "/register", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> register(@RequestPart("regUser") RegUserDto regUserDto,
                                      @RequestPart(name = "file",required = false) MultipartFile file){
        try {
            return authService.createNewUser(regUserDto, file);
        } catch (IOException e) {
            System.out.println("Failed to save profile image!");
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), "User not created, cannot save avatar file")
                    , HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest jwtRequest){
        return authService.createAuthToken(jwtRequest);
    }
}
