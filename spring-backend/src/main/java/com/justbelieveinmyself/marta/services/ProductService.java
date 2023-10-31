package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import com.justbelieveinmyself.marta.domain.dto.ProductDto;
import com.justbelieveinmyself.marta.domain.dto.ProductWithImageDto;
import com.justbelieveinmyself.marta.domain.dto.QuestionDto;
import com.justbelieveinmyself.marta.domain.dto.ReviewDto;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.entities.Question;
import com.justbelieveinmyself.marta.domain.entities.Review;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.enums.UploadDirectory;
import com.justbelieveinmyself.marta.domain.mappers.ProductMapper;
import com.justbelieveinmyself.marta.domain.mappers.QuestionMapper;
import com.justbelieveinmyself.marta.domain.mappers.ReviewMapper;
import com.justbelieveinmyself.marta.exceptions.ForbiddenException;
import com.justbelieveinmyself.marta.exceptions.NotFoundException;
import com.justbelieveinmyself.marta.exceptions.ResponseMessage;
import com.justbelieveinmyself.marta.repositories.ProductRepository;
import com.justbelieveinmyself.marta.repositories.QuestionRepository;
import com.justbelieveinmyself.marta.repositories.ReviewRepository;
import com.justbelieveinmyself.marta.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private FileHelper fileHelper;

    public ResponseEntity<?> getListProducts() {
        List<Product> products = productRepository.findAll();

        List<ProductWithImageDto> productWithImageDtoList = products.stream()
                .map(pro -> new ProductWithImageDto(productMapper.modelToDto(pro), Base64.getEncoder().encodeToString(
                        fileHelper.downloadFileAsByteArray(pro.getPreviewImg(), UploadDirectory.PRODUCTS))))
                .toList();
        return ResponseEntity.ok(productWithImageDtoList);
    }

    public ResponseEntity<?> createProduct(ProductDto productDto, MultipartFile previewImage, User currentUser) {
        validateRights(productDto, currentUser);
        String imagePath = fileHelper.uploadFile(previewImage, UploadDirectory.PRODUCTS);
        Product product = productMapper.dtoToModel(productDto);
        product.setPreviewImg(imagePath);
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.ok(productMapper.modelToDto(savedProduct));
    }

    public ResponseEntity<?> deleteProduct(Product product, User currentUser) {
        validateRights(product, currentUser);
        productRepository.delete(product);
        return ResponseEntity.ok(new ResponseMessage(200, "deleted"));
    }

    public ResponseEntity<?> updateProduct(Product productFromDb, ProductDto productDto, User currentUser) {
        validateRights(productDto, currentUser);
        BeanUtils.copyProperties(productDto, productFromDb, "id", "seller");
        return ResponseEntity.ok(productMapper.modelToDto(productRepository.save(productFromDb)));
    }

    private void validateRights(Product product, User currentUser) {
        if (!product.getSeller().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You don't have rights!");
        }
    }

    private void validateRights(ProductDto productDto, User currentUser) {
        if (!productDto.getSeller().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You don't have rights!");
        }
    }

    public ResponseEntity<?> getProduct(Product product) {
        Stream<Product> productStream = Stream.of(product);
        ProductWithImageDto productWithImageDtoList = productStream
                .map(pro -> new ProductWithImageDto(productMapper.modelToDto(pro), Base64.getEncoder().encodeToString(
                        fileHelper.downloadFileAsByteArray(pro.getPreviewImg(), UploadDirectory.PRODUCTS)))).findAny().get();
        return ResponseEntity.ok(productWithImageDtoList);
    }

    public ResponseEntity<?> getListProductReviews(Product product) {
        product.getReviews().forEach(review -> {
            List<String> base64photos = review.getPhotos().stream().map(photo
                    -> Base64.getEncoder().encodeToString(fileHelper.downloadFileAsByteArray(photo, UploadDirectory.REVIEWS))).toList();
            review.setPhotos(base64photos);
        });
        List<ReviewDto> reviews = product.getReviews().stream().map(review -> ReviewDto.of(review)).toList();
        return ResponseEntity.ok(reviews);
    }

    public ResponseEntity<?> createProductReview(ReviewDto reviewDto, User author, MultipartFile[] photos) {
        Optional<Product> productOpt = productRepository.findById(reviewDto.getProductId());
        if(productOpt.isEmpty()){
            throw new NotFoundException("Product with [%s] doesn't exists".formatted(reviewDto.getProductId()));
        }
        Product product = productOpt.get();
        Review review = reviewMapper.dtoToModel(reviewDto, product, author, photos, fileHelper);
        Review savedReview = reviewRepository.save(review);
        product.getReviews().add(savedReview);
        productRepository.save(product);
        return ResponseEntity.ok(reviewMapper.modelToDto(savedReview, fileHelper));
    }

    public ResponseEntity<?> deleteProductReview(Review review) {
        //no need to validate rights cause can be deleted only with authority "admin"
        reviewRepository.delete(review);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Successfully deleted"));
    }

    public ResponseEntity<?> getListProductQuestions(Product product) {
        List<QuestionDto> questions = product.getQuestions().stream().map(question -> questionMapper.modelToDto(question)).toList();
        return ResponseEntity.ok(questions);
    }

    public ResponseEntity<?> createProductQuestion(QuestionDto questionDto, User author) {
        Optional<Product> productOpt = productRepository.findById(questionDto.getProductId());
        if(productOpt.isEmpty()){
            throw new NotFoundException("Product with [id] doesn't exists");
        }
        Product product = productOpt.get();
        Question question = questionMapper.dtoToModel(questionDto, productRepository);
        question.setAuthor(author);
        Question savedQuestion = questionRepository.save(question);
        product.getQuestions().add(savedQuestion);
        productRepository.save(product);
        return ResponseEntity.ok(questionMapper.modelToDto(savedQuestion));
    }

    public ResponseEntity<?> getProductsFromCart(User user) {
        List<ProductDto> productDtos = user.getCartProducts().stream().map(product -> productMapper.modelToDto(product)).toList();
        return ResponseEntity.ok(productDtos);
    }

    public ResponseEntity<?> addProductToCart(Long productId, User customer) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if(productOpt.isEmpty()){
            throw new NotFoundException("Product with [id] doesn't exists");
        }
        Product product = productOpt.get();
        customer.getCartProducts().add(product);
        User savedUser = userRepository.save(customer);
        List<ProductDto> productDtos = savedUser.getCartProducts().stream().map(prod -> productMapper.modelToDto(prod)).toList();
        return ResponseEntity.ok(productDtos);
    }

    public ResponseEntity<?> deleteAllProductsInCart(User customer) {
        customer.setCartProducts(null);
        userRepository.save(customer);
        return ResponseEntity.ok(new ResponseMessage(200, "Deleted"));
    }
}