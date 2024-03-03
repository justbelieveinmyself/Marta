package com.justbelieveinmyself.marta.domain.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Images for products")
@Table(name = "product_image")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String path;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductDetail productDetail;
}
