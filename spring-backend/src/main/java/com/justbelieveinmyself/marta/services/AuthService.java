package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.domain.User;
import com.justbelieveinmyself.marta.domain.dto.JwtRequest;
import com.justbelieveinmyself.marta.domain.dto.JwtResponse;
import com.justbelieveinmyself.marta.domain.dto.RegUserDto;
import com.justbelieveinmyself.marta.domain.dto.UserDto;
import com.justbelieveinmyself.marta.exceptions.AppError;
import com.justbelieveinmyself.marta.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Bad credentials!")
                    , HttpStatus.BAD_REQUEST);
        }
        User userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtProvider.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token, userDetails.getUsername()));
    }


    public ResponseEntity<?> createNewUser(@RequestBody RegUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getPasswordConfirm())) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Passwords different")
                    , HttpStatus.BAD_REQUEST);
        }
        if (userService.findByUsername(registrationUserDto.getUsername()) != null) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "User with nickname already exists")
                    , HttpStatus.BAD_REQUEST);
        }
        User user = userService.createNewUser(registrationUserDto);
        return ResponseEntity.ok(new UserDto(user.getId(), user.getUsername(), user.getEmail()));
    }
}
