package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import com.justbelieveinmyself.marta.domain.dto.ProductWithImageDto;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.mappers.ProductMapper;
import com.justbelieveinmyself.marta.domain.mappers.QuestionMapper;
import com.justbelieveinmyself.marta.domain.mappers.ReviewMapper;
import com.justbelieveinmyself.marta.repositories.ProductRepository;
import com.justbelieveinmyself.marta.repositories.QuestionRepository;
import com.justbelieveinmyself.marta.repositories.ReviewRepository;
import com.justbelieveinmyself.marta.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @MockBean
    private ReviewRepository reviewRepository;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private QuestionRepository questionRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ProductMapper productMapper;
    @MockBean
    private QuestionMapper questionMapper;
    @MockBean
    private ReviewMapper reviewMapper;
    @MockBean
    private FileHelper fileHelper;

    @Test
    void getProductsAsPage() {
        //not worked
        List<Product> products = Arrays.asList(new Product(), new Product(), new Product());


        when(productRepository.findAll((Specification<Product>) any(), any())).thenReturn(new PageImpl<>(products));
        ResponseEntity<Page<ProductWithImageDto>> productsAsPage = productService.getProductsAsPage("id", true, 0, 5, true, false, false, null);
        assertEquals(5, productsAsPage.getBody().getSize());
        assertEquals(1, productsAsPage.getBody().getTotalElements());
    }

    @Test
    void createProduct() {
    }

    @Test
    void deleteProduct() {
    }

    @Test
    void updateProduct() {
    }

    @Test
    void getProduct() {
    }

    @Test
    void getListProductReviews() {
    }

    @Test
    void createProductReview() {
    }

    @Test
    void deleteProductReview() {
    }

    @Test
    void getListProductQuestions() {
    }

    @Test
    void createProductQuestion() {
    }

    @Test
    void getProductsFromCart() {
    }

    @Test
    void addProductToCart() {
    }

    @Test
    void deleteAllProductsInCart() {
    }

    @Test
    void deleteProductFromCart() {
    }

    @Test
    void addProductToFavourites() {
    }

    @Test
    void getProductsFromFavourites() {
    }

    @Test
    void deleteProductFromFavourites() {
    }

    @Test
    void verifyProduct() {
    }

    @Test
    void answerToReview() {
    }

    @Test
    void answerToQuestion() {
    }

    @Test
    void deleteProductQuestion() {
    }
}