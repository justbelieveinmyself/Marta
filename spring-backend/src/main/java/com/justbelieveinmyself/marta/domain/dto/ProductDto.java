package com.justbelieveinmyself.marta.domain.dto;

import com.justbelieveinmyself.marta.domain.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ProductDto {
    private Long id;
    private String productName;
    private String productCode;
    private String category;
    private BigDecimal price;
    private Long count;
    private String description;
    private String manufacturer;
    private String structure;
    private SellerDto seller;
    public static ProductDto of(Product product){
        ProductDto productDto = new ProductDto(product.getId(), product.getProductName(), product.getProductCode(), product.getCategory(), product.getPrice(), product.getCount(), product.getDescription(), product.getManufacturer(), product.getStructure(), SellerDto.of(product.getSeller()));
        return productDto;
    }
}
