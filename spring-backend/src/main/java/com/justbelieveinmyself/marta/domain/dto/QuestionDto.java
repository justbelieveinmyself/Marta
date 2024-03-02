package com.justbelieveinmyself.marta.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Information about question")
@Builder
public class QuestionDto {
    private ZonedDateTime time;
    private SellerDto author;
    private String message;
    private String answer;
    private Long productId;
}
