package com.justbelieveinmyself.marta.controllers;

import com.justbelieveinmyself.marta.domain.dto.SellerDto;
import com.justbelieveinmyself.marta.domain.dto.auth.*;
import com.justbelieveinmyself.marta.exceptions.ResponseError;
import com.justbelieveinmyself.marta.services.AuthService;
import com.justbelieveinmyself.marta.services.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/auth")
@Tag(name = "Auth", description = "The Auth API")
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthService authService, RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping(value = "/register", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Register", description = "Use this to create user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success register User.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SellerDto.class))),
            @ApiResponse(responseCode = "400", description = "1. User with nickname already exists! \n 2. Passwords different!",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResponseError.class)))
    })
    public ResponseEntity<SellerDto> register(
            @RequestPart("regUser") RegisterDto registerDTO,
            @Parameter(description = "Avatar") @RequestPart(name = "file",required = false) MultipartFile file
    ){
        return authService.createNewUser(registerDTO, file);
    }

    @PostMapping("/login")
    @Operation(summary = "Log In", description = "Use this to get refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success login.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = LoginResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Bad credentials!",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResponseError.class)))
    })
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        return authService.createAuthToken(loginRequestDto);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Tokens", description = "Use this to get access token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refresh token valid and not expired",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RefreshResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Refresh token not valid or expired!",
                    content = @Content)
    })
    public ResponseEntity<?> refreshToken(@RequestBody RefreshRequestDto refreshRequestDto){
        return ResponseEntity.ok(refreshTokenService.refreshToken(refreshRequestDto));
    }
}
