package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import com.justbelieveinmyself.marta.domain.dto.RegUserDto;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.enums.Role;
import com.justbelieveinmyself.marta.domain.enums.UploadTo;
import com.justbelieveinmyself.marta.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;
    @Autowired
    private FileHelper fileHelper;
    @Value("${upload.path}")
    private String uploadPath;
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

    public User createNewUser(RegUserDto registrationUserDto, MultipartFile file) throws IOException {
        User user = new User();
        BeanUtils.copyProperties(registrationUserDto, user, "passwordConfirm", "password");
//        user.setFirstName(registrationUserDto.getFirstName());
//        user.setLastName(registrationUserDto.getLastName());
//        user.setEmail(registrationUserDto.getEmail());
//        user.setUsername(registrationUserDto.getUsername());
//        user.setPhone(registrationUserDto.getPhone());
//        user.setCountry(registrationUserDto.getCountry());
//        user.setCity(registrationUserDto.getCity());
//        user.setAddress(registrationUserDto.getAddress());
//        user.setPostalCode(registrationUserDto.getPostalCode());
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        user.setRoles(Set.of(Role.USER));
        String path = fileHelper.uploadFile(file, UploadTo.AVATARS);
        user.setAvatar(path);
        return userRepository.save(user);
    }

    public User updateEmail(User user, String email) {
        user.setEmail(email);
        return userRepository.save(user);
    }

    public void updateAvatar(User user, MultipartFile file) throws IOException {
        String path = fileHelper.uploadFile(file, UploadTo.AVATARS);
        user.setAvatar(path);
        userRepository.save(user);
    }

    public ResponseEntity<?> getAvatar(String filename) throws IOException {
        return fileHelper.downloadFile(filename, UploadTo.AVATARS);
    }
}
