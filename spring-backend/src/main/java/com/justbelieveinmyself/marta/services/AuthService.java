package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import com.justbelieveinmyself.marta.domain.dto.SellerDto;
import com.justbelieveinmyself.marta.domain.dto.auth.LoginRequestDto;
import com.justbelieveinmyself.marta.domain.dto.auth.LoginResponseDto;
import com.justbelieveinmyself.marta.domain.dto.auth.RegisterDto;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.mappers.ProductMapper;
import com.justbelieveinmyself.marta.domain.mappers.UserMapper;
import com.justbelieveinmyself.marta.exceptions.NotCreatedException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AuthService {
    private final UserService userService;
    private final ProductMapper productMapper;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;
    private final FileHelper fileHelper;

    public AuthService(UserService userService, ProductMapper productMapper, AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService, UserMapper userMapper, FileHelper fileHelper) {
        this.userService = userService;
        this.productMapper = productMapper;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.userMapper = userMapper;
        this.fileHelper = fileHelper;
    }

    public ResponseEntity<LoginResponseDto> createAuthToken(@RequestBody LoginRequestDto authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (authRequest.getUsername(), authRequest.getPassword()));
        User userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = refreshTokenService.createToken(userDetails);
        LoginResponseDto loginResponseDTO = new LoginResponseDto(token, userMapper.modelToDto(userDetails, fileHelper, productMapper));
        return ResponseEntity.ok(loginResponseDTO);
    }


    public ResponseEntity<SellerDto> createNewUser(RegisterDto registrationUserDto, MultipartFile file) {
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
