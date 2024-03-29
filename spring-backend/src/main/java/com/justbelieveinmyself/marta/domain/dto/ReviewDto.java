package com.justbelieveinmyself.marta.domain.dto;

import com.justbelieveinmyself.marta.domain.entities.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.ZonedDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Information about review")
@Builder
public class ReviewDto {
    private Long id;
    private String message;
    private String answer;
    @Min(1) @Max(5)
    private Integer rating;
    private ZonedDateTime time;
    private List<String> photos;
    private Long productId;
    private SellerDto author;
    public static ReviewDto of(Review review){
        ReviewDto reviewDto = new ReviewDto();
        BeanUtils.copyProperties(review, reviewDto, "product", "author");
        reviewDto.setProductId(review.getProduct().getId());
        reviewDto.setAuthor(SellerDto.of(review.getAuthor()));
        reviewDto.setTime(review.getTime());
        return reviewDto;
    }
}
