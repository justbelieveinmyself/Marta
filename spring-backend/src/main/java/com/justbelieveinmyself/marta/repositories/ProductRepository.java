package com.justbelieveinmyself.marta.repositories;

import com.justbelieveinmyself.marta.domain.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByPreviewImgIsNotNull(Pageable pageable);
    Page<Product> findAllByIsVerifiedIsTrue(Pageable pageable);
    Page<Product> findAllByIsVerifiedIsTrueAndPreviewImgIsNotNull(Pageable pageable);
    Page<Product> findAllByProductNameContainingIgnoreCaseOrSeller_UsernameContainingIgnoreCase(String keyword, String firstname, Pageable pageable);
}
