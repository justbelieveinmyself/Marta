package com.justbelieveinmyself.marta.domain.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "order_product")
@Data
public class OrderProduct {
    @Id @ManyToOne @JoinColumn
    private Order order;
    @Id @ManyToOne @JoinColumn
    private Product product;
    private Integer quantity;
}
