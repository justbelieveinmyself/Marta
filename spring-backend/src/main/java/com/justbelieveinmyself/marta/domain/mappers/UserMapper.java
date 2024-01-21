package com.justbelieveinmyself.marta.domain.mappers;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import com.justbelieveinmyself.marta.domain.dto.UserDto;
import com.justbelieveinmyself.marta.domain.entities.Address;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.enums.UploadDirectory;
import org.mapstruct.*;

import java.util.Base64;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "address", ignore = true)
    UserDto modelToDto(User user, @Context FileHelper fileHelper);
    @Mapping(target = "address", ignore = true)
    User dtoToModel(UserDto userDto);

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
    default void dtoToModel(@MappingTarget User target, UserDto userDto){
        Address address = new Address(null, target, userDto.getAddress(), userDto.getAddress2(), userDto.getCity(), userDto.getPostalCode(), userDto.getCountry(), userDto.getRegion());
        target.setAddress(address);
    }
}
