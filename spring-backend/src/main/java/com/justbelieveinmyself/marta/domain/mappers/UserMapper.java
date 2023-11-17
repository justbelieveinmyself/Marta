package com.justbelieveinmyself.marta.domain.mappers;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import com.justbelieveinmyself.marta.domain.dto.ProductWithImageDto;
import com.justbelieveinmyself.marta.domain.dto.UserDto;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.enums.UploadDirectory;
import org.mapstruct.*;

import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "favouriteProducts", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    UserDto modelToDto(User user, @Context FileHelper fileHelper, @Context ProductMapper productMapper);
    @Mapping(target = "favouriteProducts", ignore = true)
    User dtoToModel(UserDto userDto);

    @AfterMapping
    default void modelToDto(@MappingTarget UserDto target, User user, @Context FileHelper fileHelper, @Context ProductMapper productMapper) {
        if (user.getFavouriteProducts() != null) {
            Set<ProductWithImageDto> dtos = user.getCartProducts().stream().map(product -> new ProductWithImageDto
                    (productMapper.modelToDto(product), (Base64.getEncoder().encodeToString(
                            fileHelper.downloadFileAsByteArray(product.getPreviewImg(), UploadDirectory.PRODUCTS)))))
                    .collect(Collectors.toSet());
            target.setFavouriteProducts(dtos);
        }
        if (user.getAvatar() != null){
            String base64Image = Base64.getEncoder().encodeToString(fileHelper.downloadFileAsByteArray(user.getAvatar(), UploadDirectory.AVATARS));
            target.setAvatar(base64Image);
        }
    }
}
