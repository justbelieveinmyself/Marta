package com.justbelieveinmyself.marta.domain.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class ProductDetailDto {
    private Long productId;
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
    private List<String> images;
}
