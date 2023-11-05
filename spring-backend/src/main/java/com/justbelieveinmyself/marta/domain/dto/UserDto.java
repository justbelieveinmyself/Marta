package com.justbelieveinmyself.marta.domain.dto;

import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.enums.Role;
import lombok.*;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"favouriteProducts"})
@ToString(exclude = {"favouriteProducts"})
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Integer age;
    private String gender;
    private Set<Role> roles;
    private String phone;
    private String address;
    private String city;
    private String postalCode;
    private String country;
    private Double balance;
    private Set<ProductWithImageDto> favouriteProducts;
}
