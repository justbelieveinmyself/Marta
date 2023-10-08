package com.justbelieveinmyself.marta.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.justbelieveinmyself.marta.domain.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    private String firstName;

    @JsonIgnore
    private String lastName;

    private String username;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private String email;

    @JsonIgnore
    private Integer age;

    @JsonIgnore
    private String gender;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnore
    private Set<Role> roles;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "seller")
    private List<Product> products;
    private String avatar;
    private String phone;
    private String address;
    private String city;
    private String postalCode;
    private String country;
    private Double balance;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
