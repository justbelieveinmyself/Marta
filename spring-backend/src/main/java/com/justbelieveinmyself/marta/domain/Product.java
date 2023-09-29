package com.justbelieveinmyself.marta.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.justbelieveinmyself.marta.Views;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "products")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Product.class)
    private Long id;
    @JsonView(Views.Product.class)
    private String name;
    @JsonView(Views.Product.class)
    private Long price;
    @JsonView(Views.Product.class)
    private Long count;
    @JsonView(Views.Product.class)
    @Lob
    @Column(length = 16777215)
    private String description;
    @JsonView(Views.Product.class)
    private String manufacturer;
    @JsonView(Views.Product.class)
    @Lob
    @Column(length = 16777215)
    private String structure;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seller_id")
    private User seller;
}
