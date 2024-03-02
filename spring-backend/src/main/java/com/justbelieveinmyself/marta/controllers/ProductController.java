package com.justbelieveinmyself.marta.controllers;

import com.justbelieveinmyself.marta.domain.annotations.CurrentUser;
import com.justbelieveinmyself.marta.domain.dto.ProductDetailDto;
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
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@Tag(name = "Product", description = "The Product API")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/page")
    @Operation(summary = "Get page of products", description = "Use this to get products. By default sort by popularity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductWithImageDto.class)))
    })
    public ResponseEntity<Page<ProductWithImageDto>> getProductsAsPage(
            @RequestParam(name = "sortOption", required = false, defaultValue = "POPULARITY") String sortOption,
            @RequestParam(name = "isAsc", required = false, defaultValue = "false") Boolean isAsc,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "12") Integer size,
            @RequestParam(name = "filterVerified", required = false, defaultValue = "false") Boolean filterVerified,
            @RequestParam(name = "filterPhotoNotNull", required = false, defaultValue = "false") Boolean filterPhotoNotNull,
            @RequestParam(name = "searchWord", required = false) String searchWord
    ) {
        return productService.getProductsAsPage(
                sortOption,
                isAsc, page, size, filterVerified,
                filterPhotoNotNull, searchWord
        );
    }

    @GetMapping("/list")
    @Operation(summary = "Get list of all or only specified seller's products", description = "Use this to get products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductWithImageDto.class)))
    })
    public ResponseEntity<List<ProductWithImageDto>> getProductsList(
            @RequestParam(required = false) Long sellerId
    ) {
        if (sellerId != null) {
            return productService.getProductsBySellerId(sellerId);
        }
        return productService.getAllProducts();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create product", description = "Use this to create product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product created",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "403", description = "1. You don't have the rights! \n 2. Preview image so big!",
                    content = @Content)
    })
    public ResponseEntity<ProductDto> createProduct(
            @RequestPart(value = "product") ProductDto productDto,
            @RequestPart(value = "productDetail", required = false) ProductDetailDto productDetailDto,
            @RequestPart(value = "file", required = false) List<MultipartFile> files,
            @CurrentUser User currentUser
    ) {
        return productService.createProduct(productDto, files, productDetailDto, currentUser);
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
    public ResponseEntity<ResponseMessage> deleteProduct(
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
    public ResponseEntity<ProductWithImageDto> getProduct(
            @Parameter(hidden = true)
            @PathVariable(value = "productId") Product product
    ) {
        return productService.getProduct(product);
    }

    @GetMapping("/detail/{productId}")
    @Operation(summary = "Get product detail by productId",description = "Returns product-details of specified product Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ProductDetail successfully returned",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "403", description = "No such product!",
                    content = @Content)
    })
    @Parameter(name = "productId", required = true, schema = @Schema(type = "integer", name = "productId"), in = ParameterIn.PATH)
    public ResponseEntity<ProductDetailDto> getProductDetail(
            @Parameter(hidden = true)
            @PathVariable(value = "productId") Product product
    ) {
        return productService.getProductDetail(product);
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
    public ResponseEntity<ProductDto> updateProduct(
            @Parameter(hidden = true) @PathVariable(value = "productId") Product productFromDb,
            @RequestBody ProductDto productDto,
            @CurrentUser User currentUser
    ) {
        return productService.updateProduct(productFromDb, productDto, currentUser);
    }

    @PutMapping("verify/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Verify product", description = "Use this to verify product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product verified",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "403", description = "You don't have the rights!",
                    content = @Content)
    })
    @Parameter(name = "productId", required = true, schema = @Schema(type = "integer", name = "productId"), in = ParameterIn.PATH)
    public ResponseEntity<ProductDto> verifyProduct(
            @Parameter(hidden = true) @PathVariable(value = "productId") Product productFromDb
    ) {
        return productService.verifyProduct(productFromDb);
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
    public ResponseEntity<List<ProductWithImageDto>> getProductsFromFavourites(
            @CurrentUser User user
    ){
        return productService.getProductsFromFavourites(user);
    }

    @DeleteMapping("favourite/{productId}")
    @Operation(summary = "Delete one product in favourites", description = "Use this to delete one product from favourites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product in Favourites successfully deleted",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "403", description = "Product from favourites doesn't deleted",
                    content = @Content)
    })
    @Parameter(name = "productId", required = true, schema = @Schema(type = "integer", name = "productId"), in = ParameterIn.PATH)
    public ResponseEntity<?> deleteProductFromFavourites(
            @CurrentUser User user,
            @Parameter(hidden = true) @PathVariable("productId") Product product
    ) {
        return productService.deleteProductFromFavourites(user, product);
    }

}
