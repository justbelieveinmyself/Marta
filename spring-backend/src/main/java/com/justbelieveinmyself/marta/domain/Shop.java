package com.justbelieveinmyself.marta.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Table(name = "shops")
@Entity
@Data
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "shops_products",
//    joinColumns = {@JoinColumn()}
//    )
    public Set<Product> products = new HashSet<>();
    public String address;
}
