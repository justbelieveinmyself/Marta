package com.justbelieveinmyself.marta.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_product")
@EqualsAndHashCode
@ToString(exclude = {"product", "quantity"})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderProduct {
    @Id @ManyToOne @JoinColumn
    private Order order;
    @Id @ManyToOne @JoinColumn
    private Product product;
    private Integer quantity;
}
