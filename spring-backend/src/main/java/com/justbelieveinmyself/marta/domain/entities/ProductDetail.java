package com.justbelieveinmyself.marta.domain.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Information about Product")
@Table(name = "product_detail")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductDetail {
    @Id
    private Long id;
    @MapsId @OneToOne
    private Product product;
    @Lob @Column(length = 16777215)
    private String description;
    @Lob @Column(length = 16777215)
    private String structure;
    private String manufacturer;
    private String dimensions;
    private Double weight;
    @Column(name = "sold_count")
    private Integer soldCount;
    private String material;
    private String color;
    @Lob @Column(name = "otherDetails", length = 16777215)
    private String otherDetails;
    @Column(name = "available")
    private Boolean isAvailable;
}
