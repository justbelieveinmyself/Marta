package com.justbelieveinmyself.marta.domain.dto;

import com.justbelieveinmyself.marta.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private LocalDate birthDate;
    private String gender;
    private Set<Role> roles;
    private String phone;
    private String address;
    private String address2;
    private String city;
    private String postalCode;
    private String region;
    private String country;
    private BigDecimal balance;
    private ZonedDateTime registeredAt;
    private String avatar;
}
