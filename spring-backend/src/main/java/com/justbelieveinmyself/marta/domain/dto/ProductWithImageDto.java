package com.justbelieveinmyself.marta.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Schema(description = "DTO for Product with image")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductWithImageDto {
    private ProductDto product;
    private String file;
}
