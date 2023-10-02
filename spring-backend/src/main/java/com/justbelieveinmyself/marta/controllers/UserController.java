package com.justbelieveinmyself.marta.controllers;

import com.justbelieveinmyself.marta.domain.User;
import com.justbelieveinmyself.marta.domain.dto.JwtResponse;
import com.justbelieveinmyself.marta.exceptions.NotFoundException;
import com.justbelieveinmyself.marta.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        return new ResponseEntity<>(response,HttpStatusCode.valueOf(200));
    }
}
