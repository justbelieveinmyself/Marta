package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.dto.JwtRequest;
import com.justbelieveinmyself.marta.domain.dto.JwtResponse;
import com.justbelieveinmyself.marta.domain.dto.RegUserDto;
import com.justbelieveinmyself.marta.domain.dto.UserDto;
import com.justbelieveinmyself.marta.exceptions.AppError;
import com.justbelieveinmyself.marta.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private AuthenticationManager authenticationManager;

    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                    (authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            System.out.println("Bad credentials!");
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Bad credentials!")
                    , HttpStatus.BAD_REQUEST);
        }
        User userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtProvider.generateToken(userDetails);
        JwtResponse jwtResponse = new JwtResponse(token, userDetails);
        return ResponseEntity.ok(jwtResponse);
    }


    public ResponseEntity<?> createNewUser(RegUserDto registrationUserDto, MultipartFile file) throws IOException {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getPasswordConfirm())) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Passwords different")
                    , HttpStatus.BAD_REQUEST);
        }
        if (userService.findByUsername(registrationUserDto.getUsername()) != null) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "User with nickname already exists")
                    , HttpStatus.BAD_REQUEST);
        }
        User user = userService.createNewUser(registrationUserDto, file);
        return ResponseEntity.ok(new UserDto(user.getId(), user.getUsername(), user.getEmail()));
    }
}
