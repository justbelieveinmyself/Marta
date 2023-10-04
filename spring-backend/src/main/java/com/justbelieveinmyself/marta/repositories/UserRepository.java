package com.justbelieveinmyself.marta.repositories;

import com.justbelieveinmyself.marta.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username) throws UsernameNotFoundException;
}
