package com.justbelieveinmyself.marta.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.justbelieveinmyself.marta.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
@Schema(description = "Information about User")
@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private String email;

    private Integer age;

    private String gender;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    private Set<Role> roles;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "seller")
    private List<Product> products;
    private String avatar;
    private String phone;
    private String address;
    private String city;
    private String postalCode;
    private String country;
    private Double balance;
    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;

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
    @JsonIgnore
    public String getCity() {
        return city;
    }

    @JsonIgnore
    public String getFirstName() {
        return firstName;
    }
    @JsonIgnore
    public String getLastName() {
        return lastName;
    }
    @JsonIgnore
    public String getPassword() {
        return password;
    }
    @JsonIgnore
    public String getEmail() {
        return email;
    }
    @JsonIgnore
    public Integer getAge() {
        return age;
    }
    @JsonIgnore
    public String getGender() {
        return gender;
    }
    @JsonIgnore
    public Set<Role> getRoles() {
        return roles;
    }
    @JsonIgnore
    public List<Product> getProducts() {
        return products;
    }
    @JsonIgnore
    public String getAvatar() {
        return avatar;
    }
    @JsonIgnore
    public String getPhone() {
        return phone;
    }
    @JsonIgnore
    public String getAddress() {
        return address;
    }
    @JsonIgnore
    public String getPostalCode() {
        return postalCode;
    }
    @JsonIgnore
    public String getCountry() {
        return country;
    }
    @JsonIgnore
    public Double getBalance() {
        return balance;
    }
    @JsonIgnore
    public RefreshToken getRefreshToken() {
        return refreshToken;
    }
}
