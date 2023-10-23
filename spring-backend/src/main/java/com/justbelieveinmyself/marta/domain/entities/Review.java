package com.justbelieveinmyself.marta.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "product_reviews")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private String answer;
    @Min(1) @Max(5) @Column(nullable = false)
    private Integer rating;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "author_id", nullable = false)
    private User author;
    @Column(nullable = false)
    private ZonedDateTime time;
    @ElementCollection @CollectionTable(name = "product_reviews_photo", joinColumns = @JoinColumn(name = "review_id"))
    private List<String> photos;
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "product_id")
    private Product product;
}