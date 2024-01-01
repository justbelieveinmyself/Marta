package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import com.justbelieveinmyself.marta.domain.dto.UserDto;
import com.justbelieveinmyself.marta.domain.dto.UserNamesDto;
import com.justbelieveinmyself.marta.domain.dto.auth.LoginResponseDto;
import com.justbelieveinmyself.marta.domain.dto.auth.RegisterDto;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.enums.Role;
import com.justbelieveinmyself.marta.domain.enums.UploadDirectory;
import com.justbelieveinmyself.marta.exceptions.ForbiddenException;
import com.justbelieveinmyself.marta.exceptions.ResponseMessage;
import com.justbelieveinmyself.marta.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
    void getAvatar_whenUsersEquals() throws MalformedURLException {
        User mockUser = User.builder().id(1L).username("user").avatar("avatar.png").build();

        when(fileHelper.downloadFileAsResponse("avatar.png", UploadDirectory.AVATARS)).thenReturn(new ResponseEntity<UrlResource>(HttpStatusCode.valueOf(200)));

        ResponseEntity<UrlResource> avatarAsResponseEntity = userService.getAvatar(mockUser, mockUser);

        assertEquals(200, avatarAsResponseEntity.getStatusCode());
        verify(fileHelper, times(1)).downloadFileAsResponse("avatar.png", UploadDirectory.AVATARS);
    }

    @Test
    void getAvatar_whenDifferentUsers() {
        User mockUser = User.builder().id(1L).username("user").avatar("avatar.png").build();
        User mockUser1 = User.builder().id(2L).username("another").build();

        assertThrows(ForbiddenException.class, () -> {
            ResponseEntity<UrlResource> avatarAsResponseEntity = userService.getAvatar(mockUser, mockUser1);
        }, "You don't have the rights!");

        verify(fileHelper, times(0)).downloadFileAsResponse(any(), any());
    }

    @Test
    void updateGender_whenUsersEquals() {
        User mockUser = User.builder().id(1L).username("user").gender("FEMALE").build();

        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<UserDto> userDtoAsResponseEntity = userService.updateGender(mockUser, "MALE", mockUser);

        assertEquals("MALE", userDtoAsResponseEntity.getBody().getGender());
        assertEquals("user", userDtoAsResponseEntity.getBody().getUsername());

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void updateGender_whenDifferentUsers() {
        User mockUser = User.builder().id(1L).username("user").gender("FEMALE").build();
        User mockUser1 = User.builder().id(2L).username("xd").build();

        assertThrows(ForbiddenException.class, () -> {
            ResponseEntity<UserDto> userDtoAsResponseEntity = userService.updateGender(mockUser, "MALE", mockUser1);
        }, "You don't have the rights!");

        verify(userRepository, times(0)).save(any());
    }

    @Test
    void updateNameAndSurname_whenUsersEquals() {
        User mockUser = User.builder().id(1L).username("user").firstName("First").lastName("Last").build();

        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<UserDto> userDtoAsResponseEntity = userService.updateNameAndSurname(mockUser, new UserNamesDto("1st", "2nd"), mockUser);

        assertEquals("1st", userDtoAsResponseEntity.getBody().getFirstName());
        assertEquals("2nd", userDtoAsResponseEntity.getBody().getLastName());

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void updateNameAndSurname_whenDifferentUsers() {
        User mockUser = User.builder().id(1L).username("user").firstName("First").lastName("Last").build();
        User mockUser1 = User.builder().id(2L).username("test").firstName("First").lastName("Last").build();

        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        assertThrows(ForbiddenException.class, () -> {
            ResponseEntity<UserDto> userDtoAsResponseEntity = userService.updateNameAndSurname(mockUser, new UserNamesDto("1st", "2nd"), mockUser1);
        }, "You don't have the rights!");

        verify(userRepository, times(0)).save(any());
    }

    @Test
    void getUser() {
        User mockUser = User.builder().id(1L).username("user").firstName("1st").lastName("2nd").build();

        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<UserDto> userDtoAsResponseEntity = userService.getUser(mockUser);

        assertEquals("1st", userDtoAsResponseEntity.getBody().getFirstName());
        assertEquals("2nd", userDtoAsResponseEntity.getBody().getLastName());
        assertEquals("user", userDtoAsResponseEntity.getBody().getUsername());
    }

    @Test
    void getAllUsers() {
        List<User> users = Arrays.asList(
                User.builder().id(1L).username("user").build(),
                User.builder().id(3L).username("admin").build(),
                User.builder().id(5L).username("test").build()
        );

        when(userRepository.findAll()).thenReturn(users);

        ResponseEntity<List<UserDto>> usersAsResponseEntity = userService.getAllUsers();

        assertEquals("user", usersAsResponseEntity.getBody().get(0).getUsername());
        assertEquals("admin", usersAsResponseEntity.getBody().get(1).getUsername());
        assertEquals("test", usersAsResponseEntity.getBody().get(2).getUsername());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void updateRoles() {
        HashSet<Role> mockRoles = new HashSet<>();
        mockRoles.add(Role.USER);
        mockRoles.add(Role.ADMIN);
        HashSet<Role> newRoles = new HashSet<>();
        newRoles.add(Role.USER);
        User mockUser = User.builder().id(1L).username("user").roles(mockRoles).build();

        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<UserDto> userDtoAsResponseEntity = userService.updateRoles(mockUser, newRoles);

        assertEquals(1, userDtoAsResponseEntity.getBody().getRoles().size());
        assertEquals("user", userDtoAsResponseEntity.getBody().getUsername());
        assertTrue(userDtoAsResponseEntity.getBody().getRoles().contains(Role.USER));

        verify(userRepository, times(1)).save(any());
    }
}