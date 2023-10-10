package com.justbelieveinmyself.marta.controllers;

import com.justbelieveinmyself.marta.domain.annotations.CurrentUser;
import com.justbelieveinmyself.marta.domain.dto.JwtRequest;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.exceptions.AppError;
import com.justbelieveinmyself.marta.exceptions.NotFoundException;
import com.justbelieveinmyself.marta.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/products")
@Tag(
        name = "Product",
        description = "The Product API"
)

public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<?> getListProducts() {
        return productService.getListProducts();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Parameter(in = ParameterIn.HEADER, schema = @Schema(type = "string", exampleClasses = {JwtRequest.class}, name = "login"))
    public ResponseEntity<?> createProduct(
            @RequestPart("product") Product product,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @CurrentUser User currentUser
    ) {
        try {
            return productService.saveProduct(product, file, currentUser);
        } catch (IOException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(),
                    "Product not created, cannot save preview file"),
                    HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{productId}")
    @Parameter(name = "productId", required = true, schema = @Schema(type = "integer", name = "productId"), in = ParameterIn.PATH)
    public ResponseEntity<?> deleteProduct(
            @Parameter(hidden = true) @PathVariable(value = "productId", required = false) Product product,
            @CurrentUser User currentUser
    ) {
        if (Objects.isNull(product))
            throw new NotFoundException("Product with [id] doesn't exists");
        return productService.deleteProduct(product, currentUser);
    }

    @Operation(description = "Returns product of specified Id")
    @GetMapping("/{productId}")
    @Parameter(name = "productId", required = true, schema = @Schema(type = "integer", name = "productId"), in = ParameterIn.PATH)
    public Product getProduct(
            @Parameter(hidden = true)
            @PathVariable(value = "productId") Product product
    ) {
        if (Objects.isNull(product))
            throw new NotFoundException("Product with [id] doesn't exists");
        return product;
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Parameter(name = "productId", required = true, schema = @Schema(type = "integer", name = "productId"), in = ParameterIn.PATH)
    public ResponseEntity<?> updateProduct(
            @Parameter(hidden = true) @PathVariable(value = "productId", required = false) Product productFromDb,
            @RequestBody Product product,
            @CurrentUser User currentUser
    ) {
        if (Objects.isNull(productFromDb)) {
            throw new NotFoundException("Product with [id] doesn't exists");
        }
        return productService.updateProduct(productFromDb, product, currentUser);
    }

}
