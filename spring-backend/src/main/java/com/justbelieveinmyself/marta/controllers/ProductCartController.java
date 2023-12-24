package com.justbelieveinmyself.marta.controllers;

import com.justbelieveinmyself.marta.domain.annotations.CurrentUser;
import com.justbelieveinmyself.marta.domain.dto.ProductDto;
import com.justbelieveinmyself.marta.domain.dto.ProductWithImageDto;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.entities.User;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/products/cart")
@Tag(name = "Product Cart", description = "The Product-Cart API")
public class ProductCartController {
    private final ProductService productService;

    public ProductCartController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "Get products from personal cart", description = "Use this to get product from his cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content)
    })
    public ResponseEntity<List<ProductWithImageDto>> getProductsFromCart(
            @CurrentUser User user
    ){
        return productService.getProductsFromCart(user);
    }

    @PostMapping(value = "{productId}")
    @Operation(summary = "Add product to personal cart", description = "Use this to add product into cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product added",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "403", description = "Product doesn't added to cart",
                    content = @Content)
    })
    @Parameter(name = "productId", required = true, schema = @Schema(type = "integer", name = "productId"), in = ParameterIn.PATH)
    public ResponseEntity<?> addProductToCart(
            @Parameter(hidden = true)
            @PathVariable("productId") Product product,
            @CurrentUser User customer
    ){
        return productService.addProductToCart(product, customer);
    }

    @DeleteMapping
    @Operation(summary = "Delete All products in cart", description = "Use this to delete all products from cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All products in Cart successfully deleted",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "403", description = "Cart doesn't deleted",
                    content = @Content)
    })
    public ResponseEntity<ResponseMessage> deleteAllProductsInCart(
            @CurrentUser User user
    ){
        return productService.deleteAllProductsInCart(user);
    }

    @DeleteMapping("{productId}")
    @Operation(summary = "Delete one product in cart", description = "Use this to delete one product from cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product in Cart successfully deleted",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "403", description = "Product from cart doesn't deleted",
                    content = @Content)
    })
    @Parameter(name = "productId", required = true, schema = @Schema(type = "integer", name = "productId"), in = ParameterIn.PATH)
    public ResponseEntity<?> deleteProductFromCart(
            @CurrentUser User user,
            @Parameter(hidden = true) @PathVariable("productId") Product product
    ) {
        return productService.deleteProductFromCart(user, product);
    }
}
