package com.justbelieveinmyself.marta.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "user")
@Builder
public class RefreshToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Builder.Default
    private String uuid = UUID.randomUUID().toString();
    private String token;
    private ZonedDateTime expiration;
    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
}
