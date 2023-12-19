package com.justbelieveinmyself.marta.domain.dto;

import com.justbelieveinmyself.marta.domain.entities.Product;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class ProductDto {
    private Long id;
    private String productName;
    private String productCode;
    private String category;
    private BigDecimal price;
    private ZonedDateTime updatedAt;
    private Long count;
    private String description;
    private Boolean isVerified;
    private String manufacturer;
    private String structure;
    private SellerDto seller;
    public static ProductDto of(Product product){
        ProductDto productDto = new ProductDto(product.getId(), product.getProductName(), product.getProductCode(), product.getCategory(), product.getPrice(), product.getUpdatedAt(), product.getCount(), product.getDescription(), product.getIsVerified(), product.getManufacturer(), product.getStructure(), SellerDto.of(product.getSeller()));
        return productDto;
    }
}
