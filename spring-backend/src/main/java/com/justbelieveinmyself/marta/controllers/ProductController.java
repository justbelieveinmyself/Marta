package com.justbelieveinmyself.marta.controllers;

import com.justbelieveinmyself.marta.domain.annotations.CurrentUser;
import com.justbelieveinmyself.marta.domain.dto.ProductDto;
import com.justbelieveinmyself.marta.domain.dto.ProductWithImageDto;
import com.justbelieveinmyself.marta.domain.dto.QuestionDto;
import com.justbelieveinmyself.marta.domain.dto.ReviewDto;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.entities.Review;
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
    @GetMapping("reviews/{productId}")
    @Operation(summary = "Get list of product reviews", description = "Use this to get all reviews of specified product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product exists",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReviewDto.class))),
            @ApiResponse(responseCode = "403", description = "Product doesn't exists!",
                    content = @Content)
    })
    @Parameter(name = "productId", required = true, schema = @Schema(type = "integer", name = "productId"), in = ParameterIn.PATH)
    public ResponseEntity<?> getProductReviews(
            @Parameter(hidden = true)
            @PathVariable(name = "productId") Product product
    ){
        return productService.getListProductReviews(product);
    }
    @PostMapping(value = "reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create product review", description = "Use this to create review for product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review saved",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReviewDto.class))),
            @ApiResponse(responseCode = "403", description = "Review doesn't saved",
                    content = @Content)
    })
    public ResponseEntity<?> createProductReview(
            @RequestPart ReviewDto reviewDto,
            @RequestPart(required = false) MultipartFile[] photos,
            @CurrentUser User author
    ){
        return productService.createProductReview(reviewDto, author, photos);
    }
    @DeleteMapping("reviews/{reviewId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete review", description = "Use this to delete review for product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review successfully deleted",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "403", description = "Review doesn't deleted",
                    content = @Content)
    })
    public ResponseEntity<?> deleteProductReview(
            @PathVariable(name = "reviewId") Review review
    ){
        return productService.deleteProductReview(review);
    }

    @GetMapping("questions/{productId}")
    @Operation(summary = "Get list of product questions", description = "Use this to get all questions of specified product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product exists",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = QuestionDto.class))),
            @ApiResponse(responseCode = "403", description = "Product doesn't exists!",
                    content = @Content)
    })
    @Parameter(name = "productId", required = true, schema = @Schema(type = "integer", name = "productId"), in = ParameterIn.PATH)
    public ResponseEntity<?> getProductQuestions(
            @Parameter(hidden = true)
            @PathVariable(name = "productId") Product product
    ){
        return productService.getListProductQuestions(product);
    }

    @PostMapping(value = "questions")
    @Operation(summary = "Create product question", description = "Use this to create question for product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question saved",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = QuestionDto.class))),
            @ApiResponse(responseCode = "403", description = "Question doesn't saved",
                    content = @Content)
    })
    public ResponseEntity<?> createProductQuestion(
            @RequestBody QuestionDto questionDto,
            @CurrentUser User author
    ){
        return productService.createProductQuestion(questionDto, author);
    }

    @GetMapping("cart")
    @Operation(summary = "Get products from personal cart", description = "Use this to get product from his cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content)
    })
    public ResponseEntity<?> getProductsFromCart(
            @CurrentUser User user
    ){
        return productService.getProductsFromCart(user);
    }

    @PostMapping(value = "cart/{productId}")
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
            @PathVariable("productId") Long productId,
            @CurrentUser User customer
    ){
        return productService.addProductToCart(productId, customer);
    }

    @DeleteMapping("cart")
    @Operation(summary = "Delete All products in cart", description = "Use this to delete all products from cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All products in Cart successfully deleted",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "403", description = "Cart doesn't deleted",
                    content = @Content)
    })
    public ResponseEntity<?> deleteAllProductsInCart(
            @CurrentUser User user
    ){
        return productService.deleteAllProductsInCart(user);
    }

    @DeleteMapping("cart/{productId}")
    @Operation(summary = "Delete one product in cart", description = "Use this to delete one product from cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product in Cart successfully deleted",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "403", description = "Product from cart doesn't deleted",
                    content = @Content)
    })
    @Parameter(name = "productId", required = true, schema = @Schema(type = "integer", name = "productId"), in = ParameterIn.PATH)
    public ResponseEntity<?> deleteProductInCart(
            @CurrentUser User user,
            @Parameter(hidden = true) @PathVariable("productId") Product product
    ) {
        return productService.deleteProductInCart(user, product);
    }

    @PostMapping(value = "favourite/{productId}")
    @Operation(summary = "Add product to favourites", description = "Use this to add product into favourites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product added",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "403", description = "Product doesn't added to favourites",
                    content = @Content)
    })
    @Parameter(name = "productId", required = true, schema = @Schema(type = "integer", name = "productId"), in = ParameterIn.PATH)
    public ResponseEntity<?> addProductToFavourites(
            @Parameter(hidden = true)
            @PathVariable("productId") Product product,
            @CurrentUser User customer
    ){
        return productService.addProductToFavourites(product, customer);
    }

    @GetMapping("favourite")
    @Operation(summary = "Get products from favourites", description = "Use this to get product from his favourites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "403", description = "Unauthorized",
                    content = @Content)
    })
    public ResponseEntity<?> getProductsFromFavourites(
            @CurrentUser User user
    ){
        return productService.getProductsFromFavourites(user);
    }
}
