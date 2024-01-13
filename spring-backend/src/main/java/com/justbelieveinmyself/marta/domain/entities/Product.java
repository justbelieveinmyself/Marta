package com.justbelieveinmyself.marta.domain.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

@Schema(description = "Information about Product")
@Table(name = "products")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString(exclude = {"customers", "followers", "orderProduct"})
@EqualsAndHashCode(exclude = {"customers", "followers", "orderProduct"})
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "product_code")
    private String productCode;
    private String category;
    private BigDecimal price;
    private Long count;
    @Lob @Column(length = 16777215)
    private String description;
    private String manufacturer;
    @Column(name = "verified")
    private Boolean isVerified;
    @UpdateTimestamp
    private ZonedDateTime updatedAt;
    @Lob @Column(length = 16777215)
    private String structure;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "seller_id")
    private User seller;
    @Column(name = "preview_image")
    private String previewImg;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<OrderProduct> orderProduct;
    @ManyToMany(mappedBy = "cartProducts")
    private Set<User> customers;
    @ManyToMany(mappedBy = "favouriteProducts")
    private Set<User> followers;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Review> reviews;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Question> questions;
    @OneToOne(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ProductDetail productDetail;
}
