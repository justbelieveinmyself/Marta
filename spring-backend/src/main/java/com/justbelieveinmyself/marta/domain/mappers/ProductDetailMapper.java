package com.justbelieveinmyself.marta.domain.mappers;

import com.justbelieveinmyself.marta.domain.dto.ProductDetailDto;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.entities.ProductDetail;
import com.justbelieveinmyself.marta.repositories.ProductRepository;
import org.mapstruct.*;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ProductDetailMapper {
    @Mapping(target = "productId", ignore = true)
    ProductDetailDto modelToDto(ProductDetail productDetail);
    @Mapping(target = "product", ignore = true)
    ProductDetail dtoToModel(ProductDetailDto productDetailDto, @Context ProductRepository productRepository);
    @AfterMapping
    default void modelToDto(@MappingTarget ProductDetailDto target, ProductDetail productDetail) {
        target.setProductId(productDetail.getProduct().getId());
    }

    @AfterMapping
    default void dtoToModel(@MappingTarget ProductDetail target, ProductDetailDto productDetailDto, @Context ProductRepository productRepository) {
        Optional<Product> productOpt = productRepository.findById(productDetailDto.getProductId());
        if(productOpt.isPresent()) {
            target.setProduct(productOpt.get());
        }

    }
}
