package com.justbelieveinmyself.marta.repositories;

import com.justbelieveinmyself.marta.domain.entities.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
}
