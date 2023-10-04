package com.justbelieveinmyself.marta.controllers;

import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.dto.JwtResponse;
import com.justbelieveinmyself.marta.exceptions.NotFoundException;
import com.justbelieveinmyself.marta.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    @GetMapping("{id}/avatar")
    public ResponseEntity<?> getAvatar(@PathVariable("id") User user) throws IOException {
        if(Objects.isNull(user)) throw new NotFoundException("User with [id] doesn't exists");
        String uploadPath = "C:/users/shadow/IdeaProjects/Marta/uploads";
        Path filePath = Paths.get(uploadPath + "/" + user.getAvatar());
        if(!Files.exists(filePath)){
            throw new FileNotFoundException(user.getAvatar());
        }
        Resource resource = new UrlResource(filePath.toUri());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("File-Name", user.getAvatar());
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;File-Name="+resource.getFilename());
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                .headers(httpHeaders).body(resource);
    }
}
