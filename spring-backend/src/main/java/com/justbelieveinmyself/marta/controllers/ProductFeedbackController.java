package com.justbelieveinmyself.marta.controllers;

import com.justbelieveinmyself.marta.domain.annotations.CurrentUser;
import com.justbelieveinmyself.marta.domain.dto.QuestionDto;
import com.justbelieveinmyself.marta.domain.dto.ReviewDto;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.entities.Question;
import com.justbelieveinmyself.marta.domain.entities.Review;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@Tag(name = "Product Feedback", description = "The Product-Feedback API")
public class ProductFeedbackController {
    private final ProductService productService;

    public ProductFeedbackController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("reviews/{productId}")
    @Operation(summary = "Get list of product reviews by productId", description = "Use this to get all reviews of specified product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product exists and got reviews",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReviewDto.class))),
            @ApiResponse(responseCode = "403", description = "Product doesn't exists!",
                    content = @Content)
    })
    @Parameter(name = "productId", required = true, schema = @Schema(type = "integer", name = "productId"), in = ParameterIn.PATH)
    public ResponseEntity<List<ReviewDto>> getProductReviews(
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
    public ResponseEntity<ReviewDto> createProductReview(
            @RequestPart ReviewDto reviewDto,
            @RequestPart(required = false) MultipartFile[] photos,
            @CurrentUser User author
    ){
        return productService.createProductReview(reviewDto, author, photos);
    }

    @PutMapping("reviews/{reviewId}")
    @Operation(summary = "Answer to review", description = "Use this to answer for review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Answered to review",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReviewDto.class))),
            @ApiResponse(responseCode = "403", description = "You don't have the rights!",
                    content = @Content)
    })
    @Parameter(name = "reviewId", required = true, schema = @Schema(type = "integer", name = "reviewId"), in = ParameterIn.PATH)
    public ResponseEntity<ReviewDto> answerToReview(
            @Parameter(hidden = true) @PathVariable(value = "reviewId") Review reviewFromDb,
            @RequestBody(required = true) String answer,
            @CurrentUser User authedUser
    ) {
        return productService.answerToReview(reviewFromDb, answer, authedUser);
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
    public ResponseEntity<ResponseMessage> deleteProductReview(
            @PathVariable(name = "reviewId") Review review
    ){
        return productService.deleteProductReview(review);
    }

    @PutMapping("questions/{questionId}")
    @Operation(summary = "Answer to question", description = "Use this to answer for question")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Answered to question",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = QuestionDto.class))),
            @ApiResponse(responseCode = "403", description = "You don't have the rights!",
                    content = @Content)
    })
    @Parameter(name = "questionId", required = true, schema = @Schema(type = "integer", name = "questionId"), in = ParameterIn.PATH)
    public ResponseEntity<QuestionDto> answerToQuestion(
            @Parameter(hidden = true) @PathVariable(value = "questionId") Question questionFromDb,
            @RequestBody(required = true) String answer,
            @CurrentUser User authedUser
    ) {
        return productService.answerToQuestion(questionFromDb, answer, authedUser);
    }

    @DeleteMapping("question/{questionId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete question", description = "Use this to delete question for product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question successfully deleted",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResponseMessage.class))),
            @ApiResponse(responseCode = "403", description = "Question doesn't deleted",
                    content = @Content)
    })
    public ResponseEntity<ResponseMessage> deleteProductQuestion(
            @PathVariable(name = "questionId") Question question
    ){
        return productService.deleteProductQuestion(question);
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
    public ResponseEntity<List<QuestionDto>> getProductQuestions(
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
    public ResponseEntity<QuestionDto> createProductQuestion(
            @RequestBody QuestionDto questionDto,
            @CurrentUser User author
    ){
        return productService.createProductQuestion(questionDto, author);
    }
}
