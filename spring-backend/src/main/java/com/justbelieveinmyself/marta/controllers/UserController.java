package com.justbelieveinmyself.marta.controllers;

import com.justbelieveinmyself.marta.domain.annotations.CurrentUser;
import com.justbelieveinmyself.marta.domain.dto.UserDto;
import com.justbelieveinmyself.marta.domain.dto.UserNamesDto;
import com.justbelieveinmyself.marta.domain.dto.auth.LoginResponseDto;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.mappers.UserMapper;
import com.justbelieveinmyself.marta.exceptions.ResponseError;
import com.justbelieveinmyself.marta.exceptions.ResponseMessage;
import com.justbelieveinmyself.marta.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/profiles")
@Tag(name = "User", description = "The User API")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @PutMapping("{profileId}/email")
    @Operation(summary = "Update email", description = "Use this to update email for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email changed",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = LoginResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "You don't have the rights!",
                    content = @Content)
    })
    @Parameter(name = "profileId", schema = @Schema(name = "profileId", type = "integer"), in = ParameterIn.PATH)
    public ResponseEntity<?> updateEmail(
            @Parameter(hidden = true) @PathVariable("profileId")User user,
            @RequestBody String email,
            @CurrentUser User currentUser
            ){
        return userService.updateEmail(user, email, currentUser);
    }

    @GetMapping("{profileId}/avatar")
    @Operation(summary = "Get avatar image", description = "Get avatar image from user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File found and delivered",
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE, array = @ArraySchema(arraySchema = @Schema(implementation = Byte.class)))),
            @ApiResponse(responseCode = "403", description = "1. You don't have the rights! \n 2. File not found!",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ResponseError.class)))
    })
    @Parameter(name = "profileId", schema = @Schema(name = "profileId", type = "integer"), in = ParameterIn.PATH)
    public ResponseEntity<?> getAvatar(
            @Parameter(hidden = true) @PathVariable("profileId") User user,
            @CurrentUser User currentUser
    ) {
        return userService.getAvatar(user, currentUser);
    }

    @PutMapping(value = "{profileId}/avatar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Update avatar image", description = "Update avatar image for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avatar updated and saved",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "403", description = "1. You don't have the rights! \n 2. Cannot update avatar!!",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ResponseError.class)))
    })
    @Parameter(name = "profileId", schema = @Schema(name = "profileId", type = "integer"), in = ParameterIn.PATH)
    public ResponseEntity<?> updateAvatar(
            @Parameter(hidden = true) @PathVariable("profileId") User user,
            @RequestParam("file") MultipartFile file,
            @CurrentUser User currentUser
    ){
        return this.userService.updateAvatar(user, file, currentUser);
    }

    @GetMapping
    public ResponseEntity<?> getUserInfo(
            @CurrentUser User user
    ){
        return ResponseEntity.ok(userMapper.modelToDto(user));
    }

    @PutMapping("{profileId}/gender")
    @Operation(summary = "Update gender", description = "Update gender value for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gender  updated and saved",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "403", description = "You don't have the rights!",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ResponseError.class)))
    })
    @Parameter(name = "profileId", schema = @Schema(name = "profileId", type = "integer"), in = ParameterIn.PATH)
    public ResponseEntity<?> updateGender(
            @Parameter(hidden = true) @PathVariable("profileId") User user,
            @RequestBody String gender,
            @CurrentUser User authedUser
    ){
        return userService.updateGender(user, gender, authedUser);
    }

    @PutMapping("{profileId}/nameAndSurname")
    @Operation(summary = "Update name and surname", description = "Update name and surname for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Name and surname updated and saved",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "403", description = "You don't have the rights!",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,schema = @Schema(implementation = ResponseError.class)))
    })
    @Parameter(name = "profileId", schema = @Schema(name = "profileId", type = "integer"), in = ParameterIn.PATH)
    public ResponseEntity<?> updateNameAndSurname(
            @Parameter(hidden = true) @PathVariable("profileId") User user,
            @RequestBody UserNamesDto userNamesDto,
            @CurrentUser User authedUser
    ){
        return userService.updateNameAndSurname(user, userNamesDto, authedUser);
    }
}
