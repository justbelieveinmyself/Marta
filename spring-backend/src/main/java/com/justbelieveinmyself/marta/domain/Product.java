package com.justbelieveinmyself.marta.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "products")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String product_code;
    private String product_name;
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
}
