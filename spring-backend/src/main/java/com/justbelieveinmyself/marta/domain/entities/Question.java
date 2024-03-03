package com.justbelieveinmyself.marta.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "product_questions")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @CreationTimestamp
    private ZonedDateTime createdAt;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "author_id", nullable = false)
    private User author;
    @Column(nullable = false)
    private String message;
    private String answer;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
