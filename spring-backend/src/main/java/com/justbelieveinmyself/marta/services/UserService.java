package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import com.justbelieveinmyself.marta.configs.beans.ProductHelper;
import com.justbelieveinmyself.marta.configs.beans.UserRightsValidator;
import com.justbelieveinmyself.marta.domain.dto.ProductWithImageDto;
import com.justbelieveinmyself.marta.domain.dto.UserDto;
import com.justbelieveinmyself.marta.domain.dto.UserNamesDto;
import com.justbelieveinmyself.marta.domain.dto.auth.LoginResponseDto;
import com.justbelieveinmyself.marta.domain.dto.auth.RegisterDto;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.enums.Role;
import com.justbelieveinmyself.marta.domain.enums.UploadDirectory;
import com.justbelieveinmyself.marta.domain.mappers.ProductMapper;
import com.justbelieveinmyself.marta.domain.mappers.UserMapper;
import com.justbelieveinmyself.marta.exceptions.ResponseMessage;
import com.justbelieveinmyself.marta.repositories.UserRepository;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductMapper productMapper;
    private final FileHelper fileHelper;
    private final UserMapper userMapper;
    private final UserRightsValidator userRightsValidator;
    private final ProductHelper productHelper;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ProductMapper productMapper, FileHelper fileHelper, UserMapper userMapper, UserRightsValidator userRightsValidator, ProductHelper productHelper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.productMapper = productMapper;
        this.fileHelper = fileHelper;
        this.userMapper = userMapper;
        this.userRightsValidator = userRightsValidator;
        this.productHelper = productHelper;
    }

    public User save(User user) {
        return userRepository.save(user);
    }


    public ResponseEntity<ResponseMessage> deleteUser(User user) {
        userRepository.delete(user);
        return ResponseEntity.ok(new ResponseMessage(200, "User successfully deleted!"));
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

    public User createNewUser(RegisterDto registrationUserDto, MultipartFile file) {
        User user = userMapper.dtoToModel(registrationUserDto, passwordEncoder);
        String path = fileHelper.uploadFile(file, UploadDirectory.AVATARS);
        user.setAvatar(path);
        return userRepository.save(user);
    }

    public ResponseEntity<LoginResponseDto> updateEmail(User user, String email, User authUser) {
        userRightsValidator.validateRights(authUser, user);
        user.setEmail(email);
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(new LoginResponseDto(null, userMapper.modelToDto(savedUser, fileHelper)));
    }

    public ResponseEntity<ResponseMessage> updateAvatar(User user, MultipartFile file, User authUser) {
        userRightsValidator.validateRights(authUser, user);
        String path = fileHelper.uploadFile(file, UploadDirectory.AVATARS);
        user.setAvatar(path);
        userRepository.save(user);
        return ResponseEntity.ok(new ResponseMessage(201, "Successfully updated!"));
    }

    public ResponseEntity<UrlResource> getAvatar(User user, User authUser) {
        userRightsValidator.validateRights(authUser, user);
        return fileHelper.downloadFileAsResponse(user.getAvatar(), UploadDirectory.AVATARS);
    }

    public ResponseEntity<UserDto> updateGender(User user, String gender, User authUser) {
        userRightsValidator.validateRights(authUser, user);
        user.setGender(gender);
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(userMapper.modelToDto(savedUser, fileHelper));
    }

    public ResponseEntity<UserDto> updateNameAndSurname(User user, UserNamesDto userNamesDto, User authedUser) {
        userRightsValidator.validateRights(authedUser, user);
        user.setFirstName(userNamesDto.getFirstName());
        user.setLastName(userNamesDto.getLastName());
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(userMapper.modelToDto(savedUser, fileHelper));
    }

    public ResponseEntity<UserDto> getUser(User user) {
        return ResponseEntity.ok(userMapper.modelToDto(user, fileHelper));
    }

    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = users.stream().map(user -> userMapper.modelToDto(user, fileHelper)).toList();
        return ResponseEntity.ok(userDtos);
    }

    public ResponseEntity<UserDto> updateRoles(User user, Set<Role> roles) {
        user.setRoles(roles);
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(userMapper.modelToDto(savedUser, fileHelper));
    }

    public ResponseEntity<List<ProductWithImageDto>> getProducts(User user, User currentUser) {
        userRightsValidator.validateRights(user, currentUser);
        List<ProductWithImageDto> productDtos = productHelper.createListOfProductsWithImageOfStream(user.getProducts().stream());
        return ResponseEntity.ok(productDtos);
    }
}
