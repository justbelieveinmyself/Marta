package com.justbelieveinmyself.marta.domain.mappers;

import com.justbelieveinmyself.marta.domain.dto.ProductDto;
import com.justbelieveinmyself.marta.domain.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto modelToDto(Product product);
    Product dtoToModel(ProductDto productDto);
    //TODO: use product details mapper
}
