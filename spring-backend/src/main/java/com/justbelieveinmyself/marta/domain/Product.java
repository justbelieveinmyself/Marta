package com.justbelieveinmyself.marta.domain;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "products")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long price;
    private Long count;
    @Lob
    @Column(length = 16777215)
    private String description;
    private String manufacturer;
    @Lob
    @Column(length = 16777215)
    private String structure;
}
