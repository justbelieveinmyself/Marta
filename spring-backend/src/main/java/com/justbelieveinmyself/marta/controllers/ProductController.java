package com.justbelieveinmyself.marta.controllers;

import com.justbelieveinmyself.marta.domain.annotations.CurrentUser;
import com.justbelieveinmyself.marta.domain.dto.ProductDto;
import com.justbelieveinmyself.marta.domain.dto.ProductWithImageDto;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.exceptions.ResponseError;
import com.justbelieveinmyself.marta.exceptions.ResponseMessage;
import com.justbelieveinmyself.marta.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @Operation(summary = "Get list of all products", description = "Use this to get products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductWithImageDto.class)))
    })
    public ResponseEntity<?> getListProducts() {
        return productService.getListProducts();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create product", description = "Use this to create product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product created",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "403", description = "1. You don't have the rights! \n 2. Preview image so big!",
                    content = @Content)
    })
    public ResponseEntity<?> createProduct(
            @RequestPart("product") ProductDto productDto,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @CurrentUser User currentUser
    ) {
        return productService.createProduct(productDto, file, currentUser);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete product", description = "Use this to delete product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "403", description = "You don't have the rights!",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResponseError.class)))
    })
    @Parameter(name = "productId", required = true, schema = @Schema(type = "integer", name = "productId"), in = ParameterIn.PATH)
    public ResponseEntity<?> deleteProduct(
            @Parameter(hidden = true) @PathVariable(value = "productId", required = false) Product product,
            @CurrentUser User currentUser
    ) {
        return productService.deleteProduct(product, currentUser);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get product by id",description = "Returns product of specified Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product exist",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "403", description = "No such product!",
                    content = @Content)
    })
    @Parameter(name = "productId", required = true, schema = @Schema(type = "integer", name = "productId"), in = ParameterIn.PATH)
    public ResponseEntity<?> getProduct(
            @Parameter(hidden = true)
            @PathVariable(value = "productId") Product product
    ) {
        return productService.getProduct(product);
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update product", description = "Use this to update product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "403", description = "You don't have the rights!",
                    content = @Content)
    })
    @Parameter(name = "productId", required = true, schema = @Schema(type = "integer", name = "productId"), in = ParameterIn.PATH)
    public ResponseEntity<?> updateProduct(
            @Parameter(hidden = true) @PathVariable(value = "productId") Product productFromDb,
            @RequestBody ProductDto productDto,
            @CurrentUser User currentUser
    ) {
        return productService.updateProduct(productFromDb, productDto, currentUser);
    }

}
