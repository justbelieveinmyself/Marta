package com.justbelieveinmyself.marta.domain.dto;

import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    public static UserDto of(User user){
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto, "password", "products", "avatar");
        return userDto;
    }
}
