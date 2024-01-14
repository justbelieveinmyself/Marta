package com.justbelieveinmyself.marta.domain.dto.auth;

import com.justbelieveinmyself.marta.domain.entities.Product;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class ProductDetailDto {
    private Long id;
    private Product product;
    private String description;
    private String structure;
    private String manufacturer;
    private String dimensions;
    private Double weight;
    private Integer soldCount;
    private String material;
    private String color;
    private String otherDetails;
    private Boolean isAvailable;
}
