package com.justbelieveinmyself.marta.domain.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "order_product")
@EqualsAndHashCode(exclude = {"product", "quantity"})
@ToString(exclude = {"product", "quantity"})
@Data
public class OrderProduct {
    @Id @ManyToOne @JoinColumn
    private Order order;
    @Id @ManyToOne @JoinColumn
    private Product product;
    private Integer quantity;
}
