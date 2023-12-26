package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @Test
    void save() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void loadUserByUsername() {
    }

    @Test
    void findByUsername() {
    }

    @Test
    void createNewUser() {
    }

    @Test
    void updateEmail() {
    }

    @Test
    void updateAvatar() {
    }

    @Test
    void getAvatar() {
    }

    @Test
    void updateGender() {
    }

    @Test
    void updateNameAndSurname() {
    }

    @Test
    void getUser() {
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void updateRoles() {
    }
}