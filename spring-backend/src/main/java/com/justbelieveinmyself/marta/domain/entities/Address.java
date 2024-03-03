package com.justbelieveinmyself.marta.domain.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
@Schema(description = "Information about Address of user")
@Entity
@Table(name = "user_address")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Address {
    @Id
    private Long id;
    @MapsId @OneToOne
    private User user;
    @Column(nullable = false)
    private String address;
    private String address2;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String postalCode;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String region;
}
