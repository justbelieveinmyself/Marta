package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.domain.dto.auth.JwtRequestDto;
import com.justbelieveinmyself.marta.domain.dto.auth.JwtResponseDto;
import com.justbelieveinmyself.marta.domain.dto.auth.RegisterDto;
import com.justbelieveinmyself.marta.domain.dto.UserDto;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.exceptions.ResponseError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RefreshTokenService refreshTokenService;
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequestDto authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                    (authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            System.out.println("Bad credentials!");
            return new ResponseEntity<>(new ResponseError(HttpStatus.UNAUTHORIZED.value(),
                    "Bad credentials!"),
                    HttpStatus.UNAUTHORIZED);
        }
        User userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = refreshTokenService.createToken(userDetails);
        JwtResponseDto jwtResponseDTO = new JwtResponseDto(token, userDetails);
        return ResponseEntity.ok(jwtResponseDTO);
    }


    public ResponseEntity<?> createNewUser(RegisterDto registrationUserDto, MultipartFile file) throws IOException {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getPasswordConfirm())) {

            return new ResponseEntity<>(new ResponseError(HttpStatus.BAD_REQUEST.value(),
                    "Passwords different"),
                    HttpStatus.BAD_REQUEST);
        }
        if (userService.findByUsername(registrationUserDto.getUsername()) != null) {
            return new ResponseEntity<>(new ResponseError(HttpStatus.BAD_REQUEST.value(),
                    "User with nickname already exists"),
                    HttpStatus.BAD_REQUEST);
        }
        User user = userService.createNewUser(registrationUserDto, file);
        return ResponseEntity.ok(new UserDto(user.getId(), user.getUsername(), user.getEmail()));
    }
}
