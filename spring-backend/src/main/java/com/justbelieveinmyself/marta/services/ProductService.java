package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.domain.Product;
import com.justbelieveinmyself.marta.repositories.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void delete(Product product) {
        productRepository.delete(product);
    }

    public Product update(Product productFromDb, Product product) {
        BeanUtils.copyProperties(product, productFromDb, "id");
        return productRepository.save(productFromDb);
    }
}
