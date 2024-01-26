package com.justbelieveinmyself.marta.domain.mappers;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import com.justbelieveinmyself.marta.domain.dto.UserDto;
import com.justbelieveinmyself.marta.domain.dto.auth.RegisterDto;
import com.justbelieveinmyself.marta.domain.entities.Address;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.enums.Role;
import com.justbelieveinmyself.marta.domain.enums.UploadDirectory;
import org.mapstruct.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "address", ignore = true)
    UserDto modelToDto(User user, @Context FileHelper fileHelper);
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "password", ignore = true)
    User dtoToModel(RegisterDto registerDto, @Context PasswordEncoder passwordEncoder);

    @AfterMapping
    default void modelToDto(@MappingTarget UserDto target, User user, @Context FileHelper fileHelper) {
        if(user.getAddress() != null) {
            target.setAddress(user.getAddress().getAddress());
            target.setAddress2(user.getAddress().getAddress2());
            target.setCity(user.getAddress().getCity());
            target.setCountry(user.getAddress().getCountry());
            target.setPostalCode(user.getAddress().getPostalCode());
            target.setRegion(user.getAddress().getRegion());
        }
        if (user.getAvatar() != null){
            String base64Image = Base64.getEncoder().encodeToString(fileHelper.downloadFileAsByteArray(user.getAvatar(), UploadDirectory.AVATARS));
            target.setAvatar(base64Image);
        }
    }

    @AfterMapping
    default void dtoToModel(@MappingTarget User target, RegisterDto registerDto, @Context PasswordEncoder passwordEncoder){
        Address address = new Address(target.getId(), target, registerDto.getAddress(), registerDto.getAddress2(), registerDto.getCity(), registerDto.getPostalCode(), registerDto.getCountry(), registerDto.getRegion());
        target.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        target.setAddress(address);
        target.setRoles(Set.of(Role.USER));
        target.setBalance(BigDecimal.valueOf(0));
        target.setRatingCount(0L);
    }
}
