package com.justbelieveinmyself.marta.domain.dto;

import com.justbelieveinmyself.marta.domain.entities.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Schema(description = "DTO for Product with image")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductWithImageDto {
    public ProductDto product;
    public String file;
}
