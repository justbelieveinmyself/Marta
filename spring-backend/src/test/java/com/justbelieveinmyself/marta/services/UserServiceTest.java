package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import com.justbelieveinmyself.marta.domain.dto.auth.LoginResponseDto;
import com.justbelieveinmyself.marta.domain.dto.auth.RegisterDto;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.enums.Role;
import com.justbelieveinmyself.marta.exceptions.ForbiddenException;
import com.justbelieveinmyself.marta.exceptions.ResponseMessage;
import com.justbelieveinmyself.marta.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private FileHelper fileHelper;
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
        RegisterDto registerDto = new RegisterDto("first", "last", "user", "123", "123", "test@mail.ru", "+79111003322", "L st.", "Las-Vegas", "42345", "USA");

        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(fileHelper.uploadFile((MultipartFile) any(), any())).thenReturn("test.png");

        User user = userService.createNewUser(registerDto, new MockMultipartFile("test.png", "test".getBytes()));

        assertEquals("first", user.getFirstName());
        assertEquals("last", user.getLastName());
        assertEquals("user", user.getUsername());
        assertEquals("test@mail.ru", user.getEmail());
        assertEquals("+79111003322", user.getPhone());
        assertEquals("L st.", user.getAddress());
        assertEquals("Las-Vegas", user.getCity());
        assertEquals("42345", user.getPostalCode());
        assertEquals("USA", user.getCountry());
        assertEquals("test.png", user.getAvatar());
        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().contains(Role.USER));
        assertTrue(Objects.nonNull(user.getPassword()));

        verify(userRepository, times(1)).save(any());
        verify(fileHelper, times(1)).uploadFile((MultipartFile) any(), any());
    }

    @Test
    void updateEmail_whenUsersEquals() {
        User mockUser = User.builder().id(1L).username("user").email("old@mail.ru").firstName("Name").build();

        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<LoginResponseDto> loginResponseDtoAsResponseEntity = userService.updateEmail(mockUser, "new@mail.ru", mockUser);

        assertNull(loginResponseDtoAsResponseEntity.getBody().getRefreshToken());
        assertEquals("user", loginResponseDtoAsResponseEntity.getBody().getUser().getUsername());
        assertEquals("new@mail.ru", loginResponseDtoAsResponseEntity.getBody().getUser().getEmail());

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void updateEmail_whenDifferentUsers() {
        User mockUser = User.builder().id(1L).username("user").email("old@mail.ru").firstName("Name").build();
        User mockUser1 = User.builder().id(2L).username("another").build();

        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        assertThrows(ForbiddenException.class, () -> {
            ResponseEntity<LoginResponseDto> loginResponseDtoAsResponseEntity = userService.updateEmail(mockUser, "new@mail.ru", mockUser1);
        });

        verify(userRepository, times(0)).save(any());
    }

    @Test
    void updateAvatar_whenUsersEquals() {
        User mockUser = User.builder().id(1L).username("user").avatar("another.png").build();

        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(fileHelper.uploadFile((MultipartFile) any(), any())).thenReturn("test.png");

        ResponseEntity<ResponseMessage> responseMessageAsResponseEntity = userService.updateAvatar(mockUser, new MockMultipartFile("test.png", "test".getBytes()), mockUser);

        assertEquals("Successfully updated!", responseMessageAsResponseEntity.getBody().getMessage());
        assertEquals(201, responseMessageAsResponseEntity.getBody().getStatus());

        verify(userRepository, times(1)).save(any());
        verify(fileHelper, times(1)).uploadFile((MultipartFile) any(), any());
    }

    @Test
    void updateAvatar_whenDifferentUsers() {
        User mockUser = User.builder().id(1L).username("user").email("old@mail.ru").firstName("Name").build();
        User mockUser1 = User.builder().id(2L).username("another").build();

        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        assertThrows(ForbiddenException.class, () -> {
            ResponseEntity<ResponseMessage> responseMessageAsResponseEntity = userService.updateAvatar(mockUser, new MockMultipartFile("test.png", "test".getBytes()), mockUser1);
        });

        verify(userRepository, times(0)).save(any());
        verify(fileHelper, times(0)).uploadFile((MultipartFile) any(), any());
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