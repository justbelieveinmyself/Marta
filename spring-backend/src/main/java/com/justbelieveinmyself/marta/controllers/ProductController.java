package com.justbelieveinmyself.marta.controllers;

import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.exceptions.AppError;
import com.justbelieveinmyself.marta.exceptions.NotFoundException;
import com.justbelieveinmyself.marta.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Value("upload.path")
    private String uploadPath;
    @GetMapping
    public ResponseEntity<?> getListProducts(){
        return productService.getListProducts();
    }
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @RequestPart("product") Product product,
            @RequestPart("file") MultipartFile file
            ){
        product.setId(null);
        try {
            Product savedProduct = productService.saveProduct(product, file);
            return ResponseEntity.ok(savedProduct);
        } catch (IOException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    "Product not created, cannot save preview file"),
                    HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteProduct(
            @PathVariable(value = "id", required = false) Product product
    ){
        if(Objects.isNull(product)) throw new NotFoundException("Product with [id] doesn't exists");
        productService.deleteProduct(product);
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public Product updateProduct(
            @PathVariable(value = "id", required = false) Product productFromDb,
            @RequestBody Product product
    ){
        if(Objects.isNull(productFromDb)) {
            throw new NotFoundException("Product with [id] doesn't exists");
        }
        Product updatedProduct = productService.updateProduct(productFromDb, product);
        return updatedProduct;
    }

}
