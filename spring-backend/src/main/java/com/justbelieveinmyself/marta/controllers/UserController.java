package com.justbelieveinmyself.marta.controllers;

import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.dto.JwtResponse;
import com.justbelieveinmyself.marta.exceptions.AppError;
import com.justbelieveinmyself.marta.exceptions.NotFoundException;
import com.justbelieveinmyself.marta.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
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
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), "Avatar not found!")
                    , HttpStatus.NOT_FOUND);
        }
        Resource resource = new UrlResource(filePath.toUri());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("File-Name", user.getAvatar());
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;File-Name="+resource.getFilename());
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                .headers(httpHeaders).body(resource);
    }
    @PutMapping(value = "{id}/avatar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateAvatar(@PathVariable("id") User user, @RequestParam("file") MultipartFile file){
        if(Objects.isNull(user)) throw new NotFoundException("User with [id] doesn't exists");
        try {
            this.userService.updateAvatar(user, file);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), "Cannot update avatar!")
                    , HttpStatus.NOT_FOUND);
        }
    }
}
