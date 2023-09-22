package com.justbelieveinmyself.marta.controllers;

import com.justbelieveinmyself.marta.domain.Product;
import com.justbelieveinmyself.marta.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @GetMapping
    public List<Product> listProducts(){
        List<Product> all = productRepository.findAll();
        return all;
    }

}
