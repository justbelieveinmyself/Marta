package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.exceptions.ResponseMessage;
import com.justbelieveinmyself.marta.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @Test
    void save() {
        User mockUser = User.builder().id(1L).firstName("Name").build();

        when(userRepository.save(mockUser)).thenAnswer(i -> i.getArguments()[0]);

        User user = userService.save(mockUser);

        assertEquals(1L, user.getId());
        assertEquals("Name", user.getFirstName());

        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void deleteUser() {
        User mockUser = User.builder().id(1L).firstName("Name").build();

        ResponseEntity<ResponseMessage> responseMessageAsResponseEntity = userService.deleteUser(mockUser);

        assertEquals("User successfully deleted!", responseMessageAsResponseEntity.getBody().getMessage());
        assertEquals(200, responseMessageAsResponseEntity.getBody().getStatus());

        verify(userRepository, times(1)).delete(mockUser);
    }

    @Test
    void loadUserByUsername_whenUserExists() {
        User mockUser = User.builder().id(1L).username("user").firstName("Name").build();

        when(userRepository.findByUsername("user")).thenReturn(mockUser);

        User user = userService.loadUserByUsername("user");

        assertEquals(1L, user.getId());
        assertEquals("Name", user.getFirstName());
        assertEquals("user", user.getUsername());

        verify(userRepository, times(1)).findByUsername("user");
    }

    @Test
    void loadUserByUsername_whenUserNotExists() {
        User mockUser = User.builder().id(1L).username("user").firstName("Name").build();

        when(userRepository.findByUsername("user")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            User user = userService.loadUserByUsername("user");
        }, "User with user not found!");

        verify(userRepository, times(1)).findByUsername("user");
    }

    @Test
    void findByUsername() {
        User mockUser = User.builder().id(1L).username("user").firstName("Name").build();

        when(userRepository.findByUsername("user")).thenReturn(mockUser);

        User user = userService.findByUsername("user");

        assertEquals(1L, user.getId());
        assertEquals("Name", user.getFirstName());
        assertEquals("user", user.getUsername());

        verify(userRepository, times(1)).findByUsername("user");
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