package com.justbelieveinmyself.marta.domain.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Schema(description = "Information about Product")
@Table(name = "products")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = "customers")
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
    @Lob @Column(length = 16777215)
    private String description;
    private String manufacturer;
    @Lob @Column(length = 16777215)
    private String structure;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "seller_id")
    private User seller;
    @Column(name = "preview_image")
    private String previewImg;
    @ManyToMany(mappedBy = "cartProducts")
    private Set<User> customers;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Review> reviews;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Question> questions;
}
