package com.justbelieveinmyself.marta.controllers;

import com.justbelieveinmyself.marta.domain.dto.JwtResponse;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.exceptions.AppError;
import com.justbelieveinmyself.marta.exceptions.NotFoundException;
import com.justbelieveinmyself.marta.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/profile")
public class UserController {
    @Autowired
    private UserService userService;
    @PutMapping("{id}/changeEmail")
    public ResponseEntity<?> updateEmail(
            @PathVariable("id")User user,
            @RequestBody String email
            ){
        if(Objects.isNull(user)) throw new NotFoundException("User with [id] doesn't exists");
        User updatedUser = userService.updateEmail(user, email);
        JwtResponse response = new JwtResponse(null,updatedUser);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(200));
    }
    @GetMapping("{id}/avatar")
    public ResponseEntity<?> getAvatar(@PathVariable("id") User user) throws IOException {
        if(Objects.isNull(user)) throw new NotFoundException("User with [id] doesn't exists");
        return userService.getAvatar(user.getAvatar());

    }
    @PutMapping(value = "{id}/avatar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateAvatar(@PathVariable("id") User user,
                                          @RequestParam("file") MultipartFile file){
        if(Objects.isNull(user)) throw new NotFoundException("User with [id] doesn't exists");
        try {
            this.userService.updateAvatar(user, file);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    "Cannot update avatar!"),
                    HttpStatus.NOT_FOUND);
        }
    }
}
