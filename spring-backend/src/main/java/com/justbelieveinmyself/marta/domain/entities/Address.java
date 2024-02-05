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
    private String address;
    private String address2;
    private String city;
    private String postalCode;
    private String country;
    private String region;
}
