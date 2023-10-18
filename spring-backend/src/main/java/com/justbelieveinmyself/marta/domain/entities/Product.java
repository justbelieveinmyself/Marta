package com.justbelieveinmyself.marta.domain.entities;

import com.justbelieveinmyself.marta.domain.dto.ProductDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Schema(description = "Information about Product")
@Table(name = "products")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "product_code")
    private String productCode;
    private String category;
    private Long price;
    private Long count;
    @Lob
    @Column(length = 16777215)
    private String description;
    private String manufacturer;
    @Lob
    @Column(length = 16777215)
    private String structure;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seller_id")
    private User seller;
    @Column(name = "preview_image")
    private String previewImg;
}
