package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import com.justbelieveinmyself.marta.domain.dto.auth.LoginResponseDto;
import com.justbelieveinmyself.marta.domain.dto.auth.RegisterDto;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.enums.Role;
import com.justbelieveinmyself.marta.domain.enums.UploadDirectory;
import com.justbelieveinmyself.marta.exceptions.ForbiddenException;
import com.justbelieveinmyself.marta.exceptions.ResponseError;
import com.justbelieveinmyself.marta.exceptions.ResponseMessage;
import com.justbelieveinmyself.marta.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;
    @Autowired
    private FileHelper fileHelper;
    public List<User> getListUsers(){
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User userFromDb = userRepository.findByUsername(username);
        if(Objects.isNull(userFromDb)) throw new UsernameNotFoundException(String.format("User with " + username + " not found!"));
        return userFromDb;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User createNewUser(RegisterDto registrationUserDto, MultipartFile file) throws IOException {
        User user = new User();
        BeanUtils.copyProperties(registrationUserDto, user, "passwordConfirm", "password");
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        user.setRoles(Set.of(Role.USER));
        String path = fileHelper.uploadFile(file, UploadDirectory.AVATARS);
        user.setAvatar(path);
        return userRepository.save(user);
    }

    public ResponseEntity<?> updateEmail(User user, String email, User authUser) {
        validateRights(authUser, user);
        user.setEmail(email);
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(LoginResponseDto.of(null, savedUser));
    }

    public ResponseEntity<?> updateAvatar(User user, MultipartFile file, User authUser) throws IOException {
        validateRights(authUser, user);
        String path = fileHelper.uploadFile(file, UploadDirectory.AVATARS);
        user.setAvatar(path);
        userRepository.save(user);
        return ResponseEntity.ok(new ResponseMessage(201, "Updated"));
    }

    public ResponseEntity<?> getAvatar(User user, User authUser) {
        validateRights(authUser, user);
        return fileHelper.downloadFile(user.getAvatar(), UploadDirectory.AVATARS);
    }

    private void validateRights(User userFromAuthToken, User userToEdit) {
        if(!userToEdit.getId().equals(userFromAuthToken.getId())){
            throw new ForbiddenException("You don't have the rights!");
        }
    }
}
