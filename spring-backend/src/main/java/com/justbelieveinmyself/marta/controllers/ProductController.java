package com.justbelieveinmyself.marta.controllers;

import com.justbelieveinmyself.marta.domain.Product;
import com.justbelieveinmyself.marta.repositories.ProductRepository;
import com.justbelieveinmyself.marta.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping
    public List<Product> getListProducts(){
        return productService.findAll();
    }
    @PostMapping
    public void createProduct(
            @RequestBody Product product
    ){
        productService.save(product);
    }
    @DeleteMapping("/{id}")
    public void deleteProduct(
            @PathVariable("id") Product product
    ){
        productService.delete(product);
    }
    @GetMapping("/{id}")
    public Product getProduct(
            @PathVariable("id") Product product
    ){
        return product;
    }
    @PutMapping("/{id}")
    public Product updateProduct(
            @PathVariable("id") Product productFromDb,
            @RequestBody Product product
    ){
        Product updatedProduct = productService.update(productFromDb, product);
        return updatedProduct;
    }

}
