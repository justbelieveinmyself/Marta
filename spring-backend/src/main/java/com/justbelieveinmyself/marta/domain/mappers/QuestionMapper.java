package com.justbelieveinmyself.marta.domain.mappers;

import com.justbelieveinmyself.marta.domain.dto.QuestionDto;
import com.justbelieveinmyself.marta.domain.entities.Question;
import com.justbelieveinmyself.marta.repositories.ProductRepository;
import org.mapstruct.*;

import java.time.ZonedDateTime;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface QuestionMapper {
    @Mapping(target = "productId", ignore = true)
    QuestionDto modelToDto(Question question);
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "time", ignore = true)
    @Mapping(target = "author", ignore = true)
    Question dtoToModel(QuestionDto questionDto, @Context ProductRepository productRepository);
    @AfterMapping
    default void modelToDto(@MappingTarget QuestionDto target, Question question){
        target.setProductId(question.getProduct().getId());
    }
    @AfterMapping
    default void dtoToModel(@MappingTarget Question target, QuestionDto questionDto, @Context ProductRepository productRepository){
        target.setProduct(productRepository.findById(questionDto.getProductId()).get());
        target.setTime(ZonedDateTime.now());
    }
}