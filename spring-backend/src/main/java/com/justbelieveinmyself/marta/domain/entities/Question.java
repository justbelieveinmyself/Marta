package com.justbelieveinmyself.marta.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table(name = "product_questions")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private ZonedDateTime time;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "author_id", nullable = false)
    private User author;
    private String message;
    private String answer;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
