//package com.justbelieveinmyself.marta.domain.mappers;
//
//import com.justbelieveinmyself.marta.domain.dto.ReviewDto;
//import com.justbelieveinmyself.marta.domain.entities.Review;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//@Mapper(componentModel = "spring")
//public interface ReviewMapper {
//    @Mapping(target = "product_id", source = "review.product")
//    ReviewDto modelToDto(Review review);
//    @Mapping(target = "product", source = "reviewDto.product_id")
//    Review dtoToModel(ReviewDto reviewDto);
//}
