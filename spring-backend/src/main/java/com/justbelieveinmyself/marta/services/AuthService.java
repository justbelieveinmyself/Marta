package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.domain.dto.UserDto;
import com.justbelieveinmyself.marta.domain.dto.auth.LoginRequestDto;
import com.justbelieveinmyself.marta.domain.dto.auth.LoginResponseDto;
import com.justbelieveinmyself.marta.domain.dto.auth.RegisterDto;
import com.justbelieveinmyself.marta.domain.dto.SellerDto;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.mappers.UserMapper;
import com.justbelieveinmyself.marta.exceptions.NotCreatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private UserMapper userMapper;
    public ResponseEntity<?> createAuthToken(@RequestBody LoginRequestDto authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (authRequest.getUsername(), authRequest.getPassword()));
        User userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = refreshTokenService.createToken(userDetails);
        LoginResponseDto loginResponseDTO = new LoginResponseDto(token, userMapper.modelToDto(userDetails));
        return ResponseEntity.ok(loginResponseDTO);
    }


    public ResponseEntity<?> createNewUser(RegisterDto registrationUserDto, MultipartFile file) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getPasswordConfirm())) {
            throw new NotCreatedException("Passwords different!");
        }
        if (userService.findByUsername(registrationUserDto.getUsername()) != null) {
            throw new NotCreatedException("User with nickname already exists!");
        }
        User user = userService.createNewUser(registrationUserDto, file);
        return ResponseEntity.ok(new SellerDto(user.getId(), user.getUsername(), user.getEmail()));
    }
}
