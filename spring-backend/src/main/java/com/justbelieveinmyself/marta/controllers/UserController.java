package com.justbelieveinmyself.marta.controllers;

import com.justbelieveinmyself.marta.domain.User;
import com.justbelieveinmyself.marta.exceptions.NotFoundException;
import com.justbelieveinmyself.marta.services.UserService;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping
    public List<User> getListUsers(){
        return userService.getListUsers();
    }
    @GetMapping("/{id}")
    public User getUser(@PathVariable(value = "id", required = false) User user){
        return user;
    }
    @PostMapping
    public User createUser(@RequestBody User user){
        user.setId(null);
        return userService.save(user);
    }
    @PutMapping("/{id}")
    public User updateUser(@RequestBody User user,
                           @PathVariable(name = "id", required = false) User userFromDB){
        if(Objects.isNull(userFromDB)) throw new NotFoundException("Product with [id] doesn't exists");
        User updated = userService.update(userFromDB, user);
        return updated;
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable(value = "id", required = false) User user){
        if(Objects.isNull(user)) throw new NotFoundException("Product with [id] doesn't exists");
        userService.delete(user);
        Map<String, Boolean> response = new HashMap<String, Boolean>();
        response.put("deleted", true);
        return ResponseEntity.ok(response);
    }
}
