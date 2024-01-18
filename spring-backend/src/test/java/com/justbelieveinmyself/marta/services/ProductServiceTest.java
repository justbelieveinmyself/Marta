package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import com.justbelieveinmyself.marta.domain.dto.*;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.entities.Question;
import com.justbelieveinmyself.marta.domain.entities.Review;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.exceptions.ResponseError;
import com.justbelieveinmyself.marta.exceptions.ResponseMessage;
import com.justbelieveinmyself.marta.repositories.ProductRepository;
import com.justbelieveinmyself.marta.repositories.QuestionRepository;
import com.justbelieveinmyself.marta.repositories.ReviewRepository;
import com.justbelieveinmyself.marta.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

        ResponseEntity<ProductDto> productDtoAsResponseEntity = productService.createProduct(mockProductDto, new MockMultipartFile("test-file", "test".getBytes()), null, mockUser);

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
        ProductDto mockProductDto = ProductDto.builder().productName("New name").id(2L).price(BigDecimal.valueOf(5)).seller(mockSellerDto).build();

        when(productRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<ProductDto> productDtoAsResponseEntity = productService.updateProduct(mockProduct, mockProductDto, mockCurrentUser);

        assertEquals("New name", productDtoAsResponseEntity.getBody().getProductName());
        assertEquals(BigDecimal.valueOf(5), productDtoAsResponseEntity.getBody().getPrice());
        assertEquals(1L, productDtoAsResponseEntity.getBody().getId());

        verify(productRepository, times(1)).save(any());
    }

    @Test
    void getProduct() {
        Product mockProduct = Product.builder().productName("Test name").id(1L).previewImg("test.png").build();

        when(fileHelper.downloadFileAsByteArray(any(), any())).thenReturn("base64encodedimage".getBytes());

        ResponseEntity<ProductWithImageDto> productWithImageDtoAsResponseEntity = productService.getProduct(mockProduct);

        assertEquals("YmFzZTY0ZW5jb2RlZGltYWdl", productWithImageDtoAsResponseEntity.getBody().getFile());
        assertEquals("Test name", productWithImageDtoAsResponseEntity.getBody().getProduct().getProductName());

        verify(fileHelper, times(1)).downloadFileAsByteArray(any(), any());
    }

    @Test
    void getListProductReviews() {
        Product mockProduct = Product.builder().productName("Test name").id(1L).previewImg("test.png").build();
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
        SellerDto sellerDto = new SellerDto(1L, "user", "test@mail.ru");
        ReviewDto mockReviewDto = new ReviewDto(1L, "test", "another", 4, ZonedDateTime.now(), null,1L,sellerDto);
        List<Review> mockReviews = new ArrayList<>();
        Product mockProduct = Product.builder().id(1L).reviews(mockReviews).build();
        MockMultipartFile[] mockImages = new MockMultipartFile[1];
        mockImages[0] = new MockMultipartFile("xd", "xd".getBytes());
        User mockUser = User.builder().id(1L).username("user").email("test@mail.ru").build();

        when(fileHelper.uploadFile((MultipartFile[]) any(), any())).thenReturn(List.of("path.png"));
        when(fileHelper.downloadFileAsByteArray(any(), any())).thenReturn("base64encodedimage".getBytes());
        when(reviewRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(mockProduct));

        ResponseEntity<ReviewDto> reviewDtoAsResponseEntity = productService.createProductReview(mockReviewDto, mockUser, mockImages);

        assertEquals("test", reviewDtoAsResponseEntity.getBody().getMessage());
        assertEquals("another", reviewDtoAsResponseEntity.getBody().getAnswer());
        assertEquals(1L, reviewDtoAsResponseEntity.getBody().getAuthor().getId());
        assertEquals("YmFzZTY0ZW5jb2RlZGltYWdl", reviewDtoAsResponseEntity.getBody().getPhotos().get(0));

        verify(fileHelper, times(1)).downloadFileAsByteArray(any(), any());
        verify(fileHelper, times(1)).uploadFile((MultipartFile[])any(), any());
        verify(reviewRepository, times(1)).save(any());
        verify(productRepository, times(1)).findById(any());
    }

    @Test
    void deleteProductReview() {

        ResponseEntity<ResponseMessage> responseMessageResponseEntity = productService.deleteProductReview(any());

        assertEquals("Successfully deleted!", responseMessageResponseEntity.getBody().getMessage());
        assertEquals(200, responseMessageResponseEntity.getBody().getStatus());

        verify(reviewRepository, times(1)).delete(any());
    }

    @Test
    void getListProductQuestions() {
        User mockUser = User.builder().id(1L).username("user").build();
        Product mockProduct = Product.builder().id(1L).build();
        List<Question> mockQuestions = new ArrayList<>();
        mockQuestions.add(new Question(1L, ZonedDateTime.now(), mockUser, "message1", "answer1", mockProduct));
        mockQuestions.add(new Question(2L, ZonedDateTime.now(), mockUser, "message2", "answer2", mockProduct));
        mockQuestions.add(new Question(5L, ZonedDateTime.now(), mockUser, "message5", "answer5", mockProduct));
        mockProduct.setQuestions(mockQuestions);
        mockUser.setProducts(List.of(mockProduct));

        ResponseEntity<List<QuestionDto>> questionsAsResponseEntity = productService.getListProductQuestions(mockProduct);

        assertEquals("message1", questionsAsResponseEntity.getBody().get(0).getMessage());
        assertEquals("answer2", questionsAsResponseEntity.getBody().get(1).getAnswer());
        assertEquals(1L, questionsAsResponseEntity.getBody().get(1).getProductId());
        assertEquals("user", questionsAsResponseEntity.getBody().get(2).getAuthor().getUsername());
    }

    @Test
    void createProductQuestion() {
        User mockUser = User.builder().id(2L).username("user").build();
        SellerDto sellerDto = new SellerDto(2L, "user", "test@mail.ru");
        List<Question> questions = new ArrayList<>();
        Product mockProduct = Product.builder().id(1L).questions(questions).build();
        QuestionDto questionDto = new QuestionDto(ZonedDateTime.now(), sellerDto, "message", "answer", 1L);

        when(productRepository.findById(any())).thenReturn(Optional.ofNullable(mockProduct));
        when(questionRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<QuestionDto> questionAsResponseEntity = productService.createProductQuestion(questionDto, mockUser);

        assertEquals("message", questionAsResponseEntity.getBody().getMessage());
        assertEquals("answer", questionAsResponseEntity.getBody().getAnswer());
        assertEquals(2L, questionAsResponseEntity.getBody().getAuthor().getId());
        assertEquals(1L, questionAsResponseEntity.getBody().getProductId());

        verify(questionRepository, times(1)).save(any());
        verify(productRepository, times(2)).findById(any());
    }

    @Test
    void getProductsFromCart() {
        Set<Product> products = new HashSet<>();
        products.add(Product.builder().id(1L).build());
        products.add(Product.builder().id(2L).build());
        products.add(Product.builder().id(5L).build());
        User mockUser = User.builder().id(2L).username("user").cartProducts(products).build();

        when(fileHelper.downloadFileAsByteArray(any(), any())).thenReturn("base64encodedimage".getBytes());

        ResponseEntity<List<ProductWithImageDto>> productsFromCartAsResponseEntity = productService.getProductsFromCart(mockUser);

        Long id = productsFromCartAsResponseEntity.getBody().get(0).getProduct().getId();
        Assertions.assertTrue(id == 1L || id == 2L || id == 5L);
        id = productsFromCartAsResponseEntity.getBody().get(2).getProduct().getId();
        Assertions.assertTrue(id == 1L || id == 2L || id == 5L);
        assertEquals("YmFzZTY0ZW5jb2RlZGltYWdl", productsFromCartAsResponseEntity.getBody().get(1).getFile());

        verify(fileHelper, times(3)).downloadFileAsByteArray(any(), any());
    }

    @Test
    void addProductToCart_whenProductAddedToCart() {
        Product mockProduct = Product.builder().id(5L).build();
        User mockUser = User.builder().id(2L).username("user").cartProducts(new HashSet<Product>()).build();

        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<?> response = productService.addProductToCart(mockProduct, mockUser);

        assertEquals(5L, ((ProductDto) response.getBody()).getId());

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void addProductToCart_whenProductAlreadyExistsInCart() {
        Product mockProduct = Product.builder().id(5L).build();
        HashSet<Product> products = new HashSet<>();
        products.add(mockProduct);
        User mockUser = User.builder().id(2L).username("user").cartProducts(products).build();

        ResponseEntity<?> response = productService.addProductToCart(mockProduct, mockUser);

        assertEquals("Already added to cart!", ((ResponseError) response.getBody()).getMessage());
        assertEquals(403, ((ResponseError) response.getBody()).getStatus().value());
    }

    @Test
    void deleteAllProductsInCart() {
        HashSet<Product> products = new HashSet<>();
        products.add(Product.builder().id(2L).build());
        products.add(Product.builder().id(5L).build());
        User mockUser = User.builder().id(2L).username("user").cartProducts(products).build();

        ResponseEntity<ResponseMessage> responseMessageAsResponseEntity = productService.deleteAllProductsInCart(mockUser);

        assertEquals("Successfully deleted!", responseMessageAsResponseEntity.getBody().getMessage());
        assertEquals(200, responseMessageAsResponseEntity.getBody().getStatus());

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void deleteProductFromCart_whenProductExistsInCart() {
        HashSet<Product> products = new HashSet<>();
        Product mockProduct = Product.builder().id(2L).build();
        products.add(mockProduct);
        products.add(Product.builder().id(5L).build());
        User mockUser = User.builder().id(2L).username("user").cartProducts(products).build();

        ResponseEntity<?> response = productService.deleteProductFromCart(mockUser, mockProduct);

        assertEquals("The product has been successfully removed from the shopping cart!", ((ResponseMessage) response.getBody()).getMessage());
        assertEquals(200, ((ResponseMessage) response.getBody()).getStatus());

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void deleteProductFromCart_whenProductDoesntExistsInCart() {
        HashSet<Product> products = new HashSet<>();
        Product mockProduct = Product.builder().id(2L).build();
        products.add(Product.builder().id(5L).build());
        User mockUser = User.builder().id(2L).username("user").cartProducts(products).build();

        ResponseEntity<?> response = productService.deleteProductFromCart(mockUser, mockProduct);

        assertEquals("This product is not in the shopping cart!", ((ResponseError) response.getBody()).getMessage());
        assertEquals(403, ((ResponseError) response.getBody()).getStatus().value());
    }

    @Test
    void addProductToFavourites_whenProductAddedToFavourites() {
        Product mockProduct = Product.builder().id(5L).build();
        User mockUser = User.builder().id(2L).username("user").favouriteProducts(new HashSet<Product>()).build();

        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<?> response = productService.addProductToFavourites(mockProduct, mockUser);

        assertEquals(5L, ((ProductDto) response.getBody()).getId());

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void addProductToFavourites_whenProductAlreadyExistsInFavourites() {
        Product mockProduct = Product.builder().id(5L).build();
        HashSet<Product> products = new HashSet<>();
        products.add(mockProduct);
        User mockUser = User.builder().id(2L).username("user").favouriteProducts(products).build();

        ResponseEntity<?> response = productService.addProductToFavourites(mockProduct, mockUser);

        assertEquals("Already added to favourites!", ((ResponseError) response.getBody()).getMessage());
        assertEquals(403, ((ResponseError) response.getBody()).getStatus().value());
    }

    @Test
    void getProductsFromFavourites() {
        Set<Product> mockFavourites = new HashSet<>();
        mockFavourites.add(Product.builder().id(1L).previewImg("test1.png").build());
        mockFavourites.add(Product.builder().id(3L).previewImg("test3.png").build());
        mockFavourites.add(Product.builder().id(5L).previewImg("test5.png").build());
        User mockUser = User.builder().id(1L).favouriteProducts(mockFavourites).build();

        when(fileHelper.downloadFileAsByteArray(any(), any())).thenReturn("base64encodedimage".getBytes());

        ResponseEntity<List<ProductWithImageDto>> favouritesAsResponseEntity = productService.getProductsFromFavourites(mockUser);

        Long id = favouritesAsResponseEntity.getBody().get(0).getProduct().getId();
        assertTrue(id == 1L || id == 3L || id == 5L);
        assertEquals("YmFzZTY0ZW5jb2RlZGltYWdl", favouritesAsResponseEntity.getBody().get(0).getFile());

        verify(fileHelper, times(3)).downloadFileAsByteArray(any(), any());
    }

    @Test
    void deleteProductFromFavourites_whenProductExistsInFavourites() {
        HashSet<Product> products = new HashSet<>();
        Product mockProduct = Product.builder().id(2L).build();
        products.add(mockProduct);
        products.add(Product.builder().id(5L).build());
        User mockUser = User.builder().id(2L).username("user").favouriteProducts(products).build();

        ResponseEntity<?> response = productService.deleteProductFromFavourites(mockUser, mockProduct);

        assertEquals("The product has been successfully removed from the favourites!", ((ResponseMessage) response.getBody()).getMessage());
        assertEquals(200, ((ResponseMessage) response.getBody()).getStatus());

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void deleteProductFromFavourites_whenProductDoesntExistsInCart() {
        HashSet<Product> products = new HashSet<>();
        Product mockProduct = Product.builder().id(2L).build();
        products.add(Product.builder().id(5L).build());
        User mockUser = User.builder().id(2L).username("user").favouriteProducts(products).build();

        ResponseEntity<?> response = productService.deleteProductFromFavourites(mockUser, mockProduct);

        assertEquals("This product is not in the favourites!", ((ResponseError) response.getBody()).getMessage());
        assertEquals(403, ((ResponseError) response.getBody()).getStatus().value());
    }

    @Test
    void verifyProduct() {
        Product mockProduct = Product.builder().id(1L).isVerified(false).build();

        when(productRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<ProductDto> productDtoAsResponseEntity = productService.verifyProduct(mockProduct);

        assertTrue(productDtoAsResponseEntity.getBody().getIsVerified());
        assertEquals(1L, productDtoAsResponseEntity.getBody().getId());

        verify(productRepository, times(1)).save(any());
    }

    @Test
    void answerToReview() {
        User mockUser = User.builder().id(2L).username("user").build();
        Product mockProduct = Product.builder().id(1L).seller(mockUser).build();
        Review mockReview =  new Review(1L, "message", "answer", 2, mockUser, ZonedDateTime.now(), null, mockProduct);
        when(reviewRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<ReviewDto> reviewDtoAsResponseEntity = productService.answerToReview(mockReview, "test", mockUser);

        assertEquals(2, reviewDtoAsResponseEntity.getBody().getRating());
        assertEquals("test", reviewDtoAsResponseEntity.getBody().getAnswer());
        assertEquals("message", reviewDtoAsResponseEntity.getBody().getMessage());

        verify(reviewRepository, times(1)).save(any());
    }

    @Test
    void answerToQuestion() {
        User mockUser = User.builder().id(2L).username("user").build();
        Product mockProduct = Product.builder().id(1L).seller(mockUser).build();
        Question mockQuestion =  new Question(1L, ZonedDateTime.now(), mockUser, "message", "answer", mockProduct);
        when(questionRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<QuestionDto> questionDtoAsResponseEntity = productService.answerToQuestion(mockQuestion, "test", mockUser);

        assertEquals("test", questionDtoAsResponseEntity.getBody().getAnswer());
        assertEquals("message", questionDtoAsResponseEntity.getBody().getMessage());

        verify(questionRepository, times(1)).save(any());
    }

    @Test
    void deleteProductQuestion() {
        ResponseEntity<ResponseMessage> responseMessageAsResponseEntity = productService.deleteProductQuestion(any());

        assertEquals("Successfully deleted!", responseMessageAsResponseEntity.getBody().getMessage());
        assertEquals(200, responseMessageAsResponseEntity.getBody().getStatus());

        verify(questionRepository, times(1)).delete(any());
    }
}