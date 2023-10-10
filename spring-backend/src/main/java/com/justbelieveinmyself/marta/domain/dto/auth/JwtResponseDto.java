package com.justbelieveinmyself.marta.domain.dto.auth;

import com.justbelieveinmyself.marta.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.Set;
@Schema(description = "DTO for response to client with token")
@Data
@AllArgsConstructor
public class JwtResponseDto {
    private String token;
    private User user;
    public JwtResponseDto(String token, com.justbelieveinmyself.marta.domain.entities.User user){
        this.token = token;
        this.user = new User();
        BeanUtils.copyProperties(user, this.user, "password", "products", "avatar");
    }
}
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class User{
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
}