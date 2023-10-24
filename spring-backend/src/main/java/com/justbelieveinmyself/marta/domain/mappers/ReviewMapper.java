package com.justbelieveinmyself.marta.domain.mappers;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import com.justbelieveinmyself.marta.domain.dto.ReviewDto;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.entities.Review;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.enums.UploadDirectory;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(target = "productId", ignore = true)
    ReviewDto modelToDto(Review review, @Context FileHelper fileHelper);

    @Mapping(target = "product", ignore = true)
    @Mapping(target = "time", ignore = true)
    @Mapping(target = "id", ignore = true)
    Review dtoToModel(ReviewDto reviewDto, Product product, User author, MultipartFile[] photos, @Context FileHelper fileHelper);

    @AfterMapping
    default void modelToDto(@MappingTarget ReviewDto target, Review review, @Context FileHelper fileHelper) {
        target.setProductId(review.getProduct().getId());
        if(review.getPhotos() != null){
            List<String> base64photos = review.getPhotos().stream().map(photo
                    -> Base64.getEncoder().encodeToString(fileHelper.downloadFileAsByteArray(photo, UploadDirectory.REVIEWS))).toList();
            target.setPhotos(base64photos);
        }
    }

    @AfterMapping
    default void dtoToModel(@MappingTarget Review target, ReviewDto reviewDto, Product product, User author,
                            MultipartFile[] photos, @Context FileHelper fileHelper) {
        target.setProduct(product);
        target.setTime(ZonedDateTime.now());
        target.setAuthor(author);
        if (photos != null) {
            List<String> uploadPaths = fileHelper.uploadFile(photos, UploadDirectory.REVIEWS);
            target.setPhotos(uploadPaths);
        }
    }
}
