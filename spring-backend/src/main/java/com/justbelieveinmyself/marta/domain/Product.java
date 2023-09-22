package com.justbelieveinmyself.marta.domain;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "products")
@Entity
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long price;
    private Long count;
    private String description;
    private String manufacturer;
    private String structure;
    public Product(){}
    public Product(String name, Long price, Long count, String description, String manufacturer, String structure) {
        super();
        this.name = name;
        this.price = price;
        this.count = count;
        this.description = description;
        this.manufacturer = manufacturer;
        this.structure = structure;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }
}
