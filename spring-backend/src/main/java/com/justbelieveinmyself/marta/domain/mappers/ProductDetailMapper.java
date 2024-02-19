package com.justbelieveinmyself.marta.domain.mappers;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import com.justbelieveinmyself.marta.domain.dto.ProductDetailDto;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.entities.ProductDetail;
import com.justbelieveinmyself.marta.domain.entities.ProductImage;
import com.justbelieveinmyself.marta.domain.enums.UploadDirectory;
import com.justbelieveinmyself.marta.repositories.ProductRepository;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ProductDetailMapper {
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "images", ignore = true)
    ProductDetailDto modelToDto(ProductDetail productDetail, @Context FileHelper fileHelper);
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "images", ignore = true)
    ProductDetail dtoToModel(ProductDetailDto productDetailDto, @Context List<MultipartFile> images, @Context ProductRepository productRepository, @Context FileHelper fileHelper);
    @AfterMapping
    default void modelToDto(@MappingTarget ProductDetailDto target, ProductDetail productDetail, @Context FileHelper fileHelper) {
        target.setProductId(productDetail.getProduct().getId());
        List<String> images = productDetail.getImages().stream().map(photo
                -> Base64.getEncoder().encodeToString(fileHelper.downloadFileAsByteArray(photo.getPath(), UploadDirectory.PRODUCTS))).toList();
        target.setImages(images);
    }

    @AfterMapping
    default void dtoToModel(@MappingTarget ProductDetail target, ProductDetailDto productDetailDto, @Context List<MultipartFile> images, @Context ProductRepository productRepository, @Context FileHelper fileHelper) {
        Optional<Product> productOpt = productRepository.findById(productDetailDto.getProductId());
        if(productOpt.isPresent()) {
            target.setProduct(productOpt.get());
        }
        if (images != null) {
            List<ProductImage> list = images.stream().map(image -> fileHelper.uploadFile(image, UploadDirectory.PRODUCTS)).map(item -> new ProductImage(null, item, target)).toList();
            target.setImages(list);
        }

    }
}
