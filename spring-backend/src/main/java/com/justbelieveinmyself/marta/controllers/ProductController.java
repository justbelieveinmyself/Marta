package com.justbelieveinmyself.marta.controllers;

import com.justbelieveinmyself.marta.domain.Product;
import com.justbelieveinmyself.marta.exceptions.NotFoundException;
import com.justbelieveinmyself.marta.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping
    public List<Product> getListProducts(){
        return productService.getListProducts();
    }
    @PostMapping
    public Product createProduct(
            @RequestBody Product product
    ){
        product.setId(null);
        return productService.save(product);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteProduct(
            @PathVariable(value = "id", required = false) Product product
    ){
        if(Objects.isNull(product)) throw new NotFoundException("Product with [id] doesn't exists");
        productService.delete(product);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", true);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public Product getProduct(
            @PathVariable(value = "id", required = false) Product product
    ){
        if(Objects.isNull(product)) throw new NotFoundException("Product with [id] doesn't exists");
        return product;
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public Product updateProduct(
            @PathVariable(value = "id", required = false) Product productFromDb,
            @RequestBody Product product
    ){
        if(Objects.isNull(productFromDb)) {
            throw new NotFoundException("Product with [id] doesn't exists");
        }
        Product updatedProduct = productService.update(productFromDb, product);
        return updatedProduct;
    }

}
