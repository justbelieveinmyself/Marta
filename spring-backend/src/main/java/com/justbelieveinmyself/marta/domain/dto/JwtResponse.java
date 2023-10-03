package com.justbelieveinmyself.marta.domain.dto;

import com.justbelieveinmyself.marta.domain.Role;
import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private User user;
    public JwtResponse(String token, com.justbelieveinmyself.marta.domain.User user){
        this.token = token;
        this.user = new User();
        this.user.setId(user.getId());
        this.user.setUsername(user.getUsername());
        this.user.setEmail(user.getEmail());
        this.user.setAge(user.getAge());
        this.user.setFirstName(user.getFirstName());
        this.user.setLastName(user.getLastName());
        this.user.setRoles(user.getRoles());
        this.user.setGender(user.getGender());
        this.user.setAvatar(user.getAvatar());
        this.user.setAddress(user.getAddress());
        this.user.setCity(user.getCity());
        this.user.setPostalCode(user.getPostalCode());
        this.user.setCountry(user.getCountry());
        this.user.setPhone(user.getPhone());
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
    private String avatar;
    private String phone;
    private String address;
    private String city;
    private String postalCode;
    private String country;
}