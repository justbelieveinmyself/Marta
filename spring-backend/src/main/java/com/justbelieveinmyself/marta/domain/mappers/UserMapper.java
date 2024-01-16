package com.justbelieveinmyself.marta.domain.mappers;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import com.justbelieveinmyself.marta.domain.dto.UserDto;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.enums.UploadDirectory;
import org.mapstruct.*;

import java.util.Base64;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "avatar", ignore = true)
    UserDto modelToDto(User user, @Context FileHelper fileHelper);
    User dtoToModel(UserDto userDto);

    @AfterMapping
    default void modelToDto(@MappingTarget UserDto target, User user, @Context FileHelper fileHelper) {
        if (user.getAvatar() != null){
            String base64Image = Base64.getEncoder().encodeToString(fileHelper.downloadFileAsByteArray(user.getAvatar(), UploadDirectory.AVATARS));
            target.setAvatar(base64Image);
        }
    }
}
