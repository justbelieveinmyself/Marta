package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import com.justbelieveinmyself.marta.domain.dto.ProductDto;
import com.justbelieveinmyself.marta.domain.dto.ProductWithImageDto;
import com.justbelieveinmyself.marta.domain.dto.ReviewDto;
import com.justbelieveinmyself.marta.domain.dto.SellerDto;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.entities.Review;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.mappers.QuestionMapper;
import com.justbelieveinmyself.marta.domain.mappers.ReviewMapper;
import com.justbelieveinmyself.marta.exceptions.ResponseMessage;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    private QuestionMapper questionMapper;
    @MockBean
    private ReviewMapper reviewMapper;
    @MockBean
    private FileHelper fileHelper;

    @Test
    void getProductsAsPage() {
        List<Product> products = Arrays.asList(new Product(), new Product(), new Product());


        when(productRepository.findAll((Specification<Product>) any(), any())).thenReturn(new PageImpl<>(products));
        when(fileHelper.downloadFileAsByteArray(any(), any())).thenReturn("".getBytes());


        ResponseEntity<Page<ProductWithImageDto>> productsAsPage = productService.getProductsAsPage("id", true, 0, 5, true, false, false, null);

        assertEquals(5, productsAsPage.getBody().getSize());
        assertEquals(3, productsAsPage.getBody().getTotalElements());
        verify(productRepository, times(1)).findAll((Specification<Product>) any(), any());
    }

    @Test
    void createProduct() {
        User mockUser = User.builder().username("user").id(1L).email("test@mail.ru").build();
        ProductDto mockProductDto = ProductDto.builder().productName("Test Name").id(1L).build();
        Product mockProduct = Product.builder().productName("Test Name").id(1L).build();

        when(productRepository.save(any())).thenReturn(mockProduct);
        when(fileHelper.uploadFile((MultipartFile) any(), any())).thenReturn("test-path.png");

        ResponseEntity<ProductDto> productDtoAsResponseEntity = productService.createProduct(mockProductDto, new MockMultipartFile("test-file", "test".getBytes()), mockUser);

        assertEquals("Test Name", productDtoAsResponseEntity.getBody().getProductName());
        assertEquals(1L, productDtoAsResponseEntity.getBody().getId());
        verify(productRepository, times(1)).save(any());
        verify(fileHelper, times(1)).uploadFile((MultipartFile) any(), any());
    }

    @Test
    void deleteProduct() {
        User mockUser = User.builder().username("user").id(1L).email("test@mail.ru").build();
        Product mockProduct = Product.builder().productName("Test Name").id(1L).build();
        mockProduct.setSeller(mockUser);

        ResponseEntity<ResponseMessage> responseMessageAsResponseEntity = productService.deleteProduct(mockProduct, mockUser);

        assertEquals("Successfully deleted!", responseMessageAsResponseEntity.getBody().getMessage());
        assertEquals(200, responseMessageAsResponseEntity.getBody().getStatus());
        verify(productRepository, times(1)).delete(any());
    }

    @Test
    void updateProduct() {
        User mockCurrentUser = User.builder().username("user").id(1L).email("test@mail.ru").build();
        SellerDto mockSellerDto = SellerDto.builder().username("user").id(1L).email("test@mail.ru").build();
        Product mockProduct = Product.builder().productName("Test Name").id(1L).build();
        ProductDto mockProductDto = ProductDto.builder().productName("New name").id(2L).description("Some description").price(BigDecimal.valueOf(5)).seller(mockSellerDto).build();

        when(productRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<ProductDto> productDtoAsResponseEntity = productService.updateProduct(mockProduct, mockProductDto, mockCurrentUser);

        assertEquals("New name", productDtoAsResponseEntity.getBody().getProductName());
        assertEquals("Some description", productDtoAsResponseEntity.getBody().getDescription());
        assertEquals(BigDecimal.valueOf(5), productDtoAsResponseEntity.getBody().getPrice());
        assertEquals(1L, productDtoAsResponseEntity.getBody().getId());
        verify(productRepository, times(1)).save(any());
    }

    @Test
    void getProduct() {
        Product mockProduct = Product.builder().productName("Test name").id(1L).description("test").previewImg("test.png").build();

        when(fileHelper.downloadFileAsByteArray(any(), any())).thenReturn("base64encodedimage".getBytes());

        ResponseEntity<ProductWithImageDto> productWithImageDtoAsResponseEntity = productService.getProduct(mockProduct);

        assertEquals("YmFzZTY0ZW5jb2RlZGltYWdl", productWithImageDtoAsResponseEntity.getBody().getFile());
        assertEquals("Test name", productWithImageDtoAsResponseEntity.getBody().getProduct().getProductName());
        verify(fileHelper, times(1)).downloadFileAsByteArray(any(), any());
    }

    @Test
    void getListProductReviews() {
        Product mockProduct = Product.builder().productName("Test name").id(1L).description("test").previewImg("test.png").build();
        List<String> mockImages = List.of("test.png", "test2.png");
        User mockUser = User.builder().id(1L).username("user").build();
        List<Review> mockReviews = List.of(
                new Review(1L, "Test", "Answer", 4, mockUser, ZonedDateTime.now(), mockImages, mockProduct),
                new Review(2L, "New", "Answer2", 2, mockUser, ZonedDateTime.now(), mockImages, mockProduct)
        );
        mockProduct.setReviews(mockReviews);
        when(fileHelper.downloadFileAsByteArray(any(), any())).thenReturn("base64encodedimage".getBytes());

        ResponseEntity<List<ReviewDto>> reviewsAsResponseEntity = productService.getListProductReviews(mockProduct);

        assertEquals("Test", reviewsAsResponseEntity.getBody().get(0).getMessage());
        assertEquals("Answer2", reviewsAsResponseEntity.getBody().get(1).getAnswer());
        assertEquals(2, reviewsAsResponseEntity.getBody().get(1).getRating());
        assertEquals("YmFzZTY0ZW5jb2RlZGltYWdl", reviewsAsResponseEntity.getBody().get(0).getPhotos().get(0));
        verify(fileHelper, times(4)).downloadFileAsByteArray(any(), any());
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