package com.justbelieveinmyself.marta.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String productName;
    private String productCode;
    private String category;
    private Long price;
    private Long count;
    private String description;
    private String manufacturer;
    private String structure;
    private SellerDto seller;
}
