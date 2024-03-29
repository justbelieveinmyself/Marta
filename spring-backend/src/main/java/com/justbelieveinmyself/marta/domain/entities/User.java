package com.justbelieveinmyself.marta.domain.entities;

import com.justbelieveinmyself.marta.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Schema(description = "Information about User")
@Entity
@Table(name = "users")
@Data
@Builder
@ToString(exclude = {"cartProducts", "favouriteProducts", "address"})
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"cartProducts", "favouriteProducts"})
public class User implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private LocalDate birthDate;
    private String gender;
    @Enumerated(EnumType.STRING) @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    private Set<Role> roles;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "seller")
    private List<Product> products;
    private String avatar;
    private String phone;
    private BigDecimal balance;
    @CreationTimestamp
    private ZonedDateTime registeredAt;
    private Long ratingCount;
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Address address;
    @ManyToMany
    @JoinTable(name = "product_cart", joinColumns = @JoinColumn(name = "customer_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> cartProducts;
    @ManyToMany
    @JoinTable(name = "product_favourites", joinColumns = @JoinColumn(name = "follower_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> favouriteProducts;
    @OneToOne(mappedBy = "user", cascade = CascadeType.DETACH)
    private RefreshToken refreshToken;
    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

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
