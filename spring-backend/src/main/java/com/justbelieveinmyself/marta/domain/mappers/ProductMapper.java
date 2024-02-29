package com.justbelieveinmyself.marta.domain.mappers;

import com.justbelieveinmyself.marta.domain.dto.ProductDto;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.entities.Review;
import org.mapstruct.AfterMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ProductMapper {
    ProductDto modelToDto(Product product);
    Product dtoToModel(ProductDto productDto);

    @AfterMapping
    default void modelToDto(@MappingTarget ProductDto target, Product product) {
        List<Review> reviews = product.getReviews();
        if (reviews != null) {
            double sum = reviews.stream().mapToInt(Review::getRating).sum();
            double averageRate = sum / reviews.size();
            target.setAverageRate(averageRate);
        }
    }

}
