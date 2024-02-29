package com.justbelieveinmyself.marta.domain.dto;

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
    private Double averageRate;
    private String category;
    private BigDecimal price;
    private Integer discountPercentage;
    private Boolean isVerified;
    private ZonedDateTime updatedAt;
    private SellerDto seller;
}
