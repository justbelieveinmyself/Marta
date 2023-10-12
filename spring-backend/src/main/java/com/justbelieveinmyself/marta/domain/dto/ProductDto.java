package com.justbelieveinmyself.marta.domain.dto;

import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

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
    private UserDto seller;
    public static ProductDto of(Product product){
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto, "previewImg", "seller");
        productDto.setSeller(UserDto.of(product.getSeller()));
        return productDto;
    }
}
