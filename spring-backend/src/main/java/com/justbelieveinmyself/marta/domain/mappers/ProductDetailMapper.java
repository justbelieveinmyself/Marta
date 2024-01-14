package com.justbelieveinmyself.marta.domain.mappers;

import com.justbelieveinmyself.marta.domain.dto.auth.ProductDetailDto;
import com.justbelieveinmyself.marta.domain.entities.ProductDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductDetailMapper {
    ProductDetailDto modelToDto(ProductDetail productDetail);
    ProductDetail dtoToModel(ProductDetailDto productDetailDto);
}
