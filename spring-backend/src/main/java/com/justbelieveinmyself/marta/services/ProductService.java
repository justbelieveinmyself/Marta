package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import com.justbelieveinmyself.marta.configs.beans.ProductHelper;
import com.justbelieveinmyself.marta.configs.beans.UserRightsValidator;
import com.justbelieveinmyself.marta.domain.dto.*;
import com.justbelieveinmyself.marta.domain.entities.*;
import com.justbelieveinmyself.marta.domain.enums.UploadDirectory;
import com.justbelieveinmyself.marta.domain.mappers.ProductDetailMapper;
import com.justbelieveinmyself.marta.domain.mappers.ProductMapper;
import com.justbelieveinmyself.marta.domain.mappers.QuestionMapper;
import com.justbelieveinmyself.marta.domain.mappers.ReviewMapper;
import com.justbelieveinmyself.marta.exceptions.NotFoundException;
import com.justbelieveinmyself.marta.exceptions.ResponseError;
import com.justbelieveinmyself.marta.exceptions.ResponseMessage;
import com.justbelieveinmyself.marta.repositories.*;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
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
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    private final ProductMapper productMapper;
    private final ProductHelper productHelper;
    private final ProductDetailMapper productDetailMapper;
    private final QuestionMapper questionMapper;
    private final ReviewMapper reviewMapper;
    private final FileHelper fileHelper;
    private final UserRightsValidator userRightsValidator;

    public ProductService(ProductRepository productRepository, ReviewRepository reviewRepository, QuestionRepository questionRepository, UserRepository userRepository, ProductDetailRepository productDetailRepository, ProductMapper productMapper, ProductHelper productHelper, ProductDetailMapper productDetailMapper, QuestionMapper questionMapper, ReviewMapper reviewMapper, FileHelper fileHelper, UserRightsValidator userRightsValidator) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.productMapper = productMapper;
        this.productHelper = productHelper;
        this.productDetailMapper = productDetailMapper;
        this.questionMapper = questionMapper;
        this.reviewMapper = reviewMapper;
        this.fileHelper = fileHelper;
        this.userRightsValidator = userRightsValidator;
    }

    public ResponseEntity<Page<ProductWithImageDto>> getProductsAsPage(
            String sortBy,
            Boolean isAsc,
            Integer page,
            Integer size,
            Boolean usePages,
            Boolean filterVerified,
            Boolean filterPhotoNotNull,
            String searchWord
    ) {
        if (usePages) {
            Pageable pageable = PageRequest.of(page, size);
            Specification<Product> specification = productHelper.createSpecification(sortBy, isAsc, filterPhotoNotNull, filterVerified, searchWord);
            Page<Product> products = productRepository.findAll(specification, pageable);
            List<ProductWithImageDto> productWithImageDtoList = productHelper.createListOfProductsWithImageOfStream(products.stream());

            return ResponseEntity.ok(new PageImpl<>(productWithImageDtoList, pageable, products.getTotalElements()));
        }
        Pageable pageRequest = PageRequest.of(0, Integer.MAX_VALUE);
        Page<Product> products = productRepository.findAll(pageRequest);
        List<ProductWithImageDto> productWithImageDtoList = productHelper.createListOfProductsWithImageOfStream(products.stream());

        return ResponseEntity.ok(new PageImpl<>(productWithImageDtoList, pageRequest, products.getTotalElements()));
    }

    public ResponseEntity<ProductDto> createProduct(ProductDto productDto, List<MultipartFile> images, ProductDetailDto productDetailDto, User currentUser) {
        Product product = productMapper.dtoToModel(productDto);
        product.setSeller(currentUser);
        if (productDetailDto != null) {
            ProductDetail productDetail = productDetailMapper.dtoToModel(productDetailDto, images, productRepository, fileHelper);
            productDetail.setProduct(product);
            product.setProductDetail(productDetail);
        }
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.ok(productMapper.modelToDto(savedProduct));
    }

    public ResponseEntity<ResponseMessage> deleteProduct(Product product, User currentUser) {
        userRightsValidator.validateRightsOrAdminRole(product, currentUser);
        productRepository.delete(product);
        return ResponseEntity.ok(new ResponseMessage(200, "Successfully deleted!"));
    }

    public ResponseEntity<ProductDto> updateProduct(Product productFromDb, ProductDto productDto, User currentUser) {
        userRightsValidator.validateRights(productDto, currentUser);
        BeanUtils.copyProperties(productDto, productFromDb, "id", "seller");
        Product savedProduct = productRepository.save(productFromDb);
        return ResponseEntity.ok(productMapper.modelToDto(savedProduct));
    }

    public ResponseEntity<ProductWithImageDto> getProduct(Product product) {
        Stream<Product> productStream = Stream.of(product);
        ProductWithImageDto productWithImageDtoList = productHelper.createListOfProductsWithImageOfStream(productStream).get(0);
        return ResponseEntity.ok(productWithImageDtoList);
    }

    public ResponseEntity<List<ReviewDto>> getListProductReviews(Product product) {
        product.getReviews().forEach(review -> {
            List<String> base64photos = review.getPhotos().stream().map(photo
                    -> Base64.getEncoder().encodeToString(fileHelper.downloadFileAsByteArray(photo, UploadDirectory.REVIEWS))).toList();
            review.setPhotos(base64photos);
        });
        List<ReviewDto> reviews = product.getReviews().stream().map(review -> ReviewDto.of(review)).toList();
        return ResponseEntity.ok(reviews);
    }

    public ResponseEntity<ReviewDto> createProductReview(ReviewDto reviewDto, User author, MultipartFile[] photos) {
        Optional<Product> productOpt = productRepository.findById(reviewDto.getProductId());
        if (productOpt.isEmpty()) {
            throw new NotFoundException("Product with [%s] doesn't exists".formatted(reviewDto.getProductId()));
        }
        Product product = productOpt.get();
        Review review = reviewMapper.dtoToModel(reviewDto, product, author, photos, fileHelper);
        Review savedReview = reviewRepository.save(review);
        product.getReviews().add(savedReview);
        productRepository.save(product);
        return ResponseEntity.ok(reviewMapper.modelToDto(savedReview, fileHelper));
    }

    public ResponseEntity<ResponseMessage> deleteProductReview(Review review) {
        //no need to validate rights cause can be deleted only with authority "admin"
        reviewRepository.delete(review);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Successfully deleted!"));
    }

    public ResponseEntity<List<QuestionDto>> getListProductQuestions(Product product) {
        List<QuestionDto> questions = product.getQuestions().stream().map(question -> questionMapper.modelToDto(question)).toList();
        return ResponseEntity.ok(questions);
    }

    public ResponseEntity<QuestionDto> createProductQuestion(QuestionDto questionDto, User author) {
        Optional<Product> productOpt = productRepository.findById(questionDto.getProductId());
        if (productOpt.isEmpty()) {
            throw new NotFoundException("Product with [id] doesn't exists");
        }
        Product product = productOpt.get();
        Question question = questionMapper.dtoToModel(questionDto, productRepository);
        question.setAuthor(author);
        Question savedQuestion = questionRepository.save(question);
        product.getQuestions().add(savedQuestion);
        productRepository.save(product);
        QuestionDto questionDto1 = questionMapper.modelToDto(savedQuestion);
        return ResponseEntity.ok(questionDto1);
    }

    public ResponseEntity<List<ProductWithImageDto>> getProductsFromCart(User user) {
        List<ProductWithImageDto> productDtos = productHelper.createListOfProductsWithImageOfStream(user.getCartProducts().stream());
        return ResponseEntity.ok(productDtos);
    }

    public ResponseEntity<?> addProductToCart(Product product, User customer) {
        if (customer.getCartProducts().add(product)) {
            userRepository.save(customer);
            return ResponseEntity.ok(productMapper.modelToDto(product));
        } else {
            ResponseError responseError = new ResponseError(HttpStatus.FORBIDDEN, "Already added to cart!");
            return new ResponseEntity<>(responseError, HttpStatus.FORBIDDEN);
        }

    }

    public ResponseEntity<ResponseMessage> deleteAllProductsInCart(User customer) {
        customer.setCartProducts(null);
        userRepository.save(customer);
        return ResponseEntity.ok(new ResponseMessage(200, "Successfully deleted!"));
    }

    public ResponseEntity<?> deleteProductFromCart(User customer, Product product) {
        if (customer.getCartProducts().remove(product)) {
            userRepository.save(customer);
            return ResponseEntity.ok(new ResponseMessage(200, "The product has been successfully removed from the shopping cart!"));
        } else {
            ResponseError responseError = new ResponseError(HttpStatus.FORBIDDEN, "This product is not in the shopping cart!");
            return new ResponseEntity<>(responseError, HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<?> addProductToFavourites(Product product, User customer) {
        if (customer.getFavouriteProducts().add(product)) {
            userRepository.save(customer);
            return ResponseEntity.ok(productMapper.modelToDto(product));
        } else {
            ResponseError responseError = new ResponseError(HttpStatus.FORBIDDEN, "Already added to favourites!");
            return new ResponseEntity<>(responseError, HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<List<ProductWithImageDto>> getProductsFromFavourites(User user) {
        Set<Product> favouriteProducts = user.getFavouriteProducts();
        List<ProductWithImageDto> productDtos = productHelper.createListOfProductsWithImageOfStream(favouriteProducts.stream());
        return ResponseEntity.ok(productDtos);
    }



    public ResponseEntity<?> deleteProductFromFavourites(User user, Product product) {
        if (user.getFavouriteProducts().remove(product)) {
            userRepository.save(user);
            return ResponseEntity.ok(new ResponseMessage(200, "The product has been successfully removed from the favourites!"));
        } else {
            ResponseError responseError = new ResponseError(HttpStatus.FORBIDDEN, "This product is not in the favourites!");
            return new ResponseEntity<>(responseError, HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<ProductDto> verifyProduct(Product productFromDb) {
        productFromDb.setIsVerified(true);
        Product savedProduct = productRepository.save(productFromDb);
        return ResponseEntity.ok(productMapper.modelToDto(savedProduct));
    }

    public ResponseEntity<ReviewDto> answerToReview(Review reviewFromDb, String answer, User authedUser) {
        userRightsValidator.validateRights(reviewFromDb.getProduct(), authedUser);
        reviewFromDb.setAnswer(answer);
        Review savedReview = reviewRepository.save(reviewFromDb);
        return ResponseEntity.ok(reviewMapper.modelToDto(savedReview, fileHelper));
    }

    public ResponseEntity<QuestionDto> answerToQuestion(Question questionFromDb, String answer, User authedUser) {
        userRightsValidator.validateRights(questionFromDb.getProduct(), authedUser);
        questionFromDb.setAnswer(answer);
        Question savedQuestion = questionRepository.save(questionFromDb);
        return ResponseEntity.ok(questionMapper.modelToDto(savedQuestion));
    }

    public ResponseEntity<ResponseMessage> deleteProductQuestion(Question question) {
        //no need to validate rights cause can be deleted only with authority "admin"
        questionRepository.delete(question);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Successfully deleted!"));
    }

    public ResponseEntity<ProductDetailDto> getProductDetail(Product product) {
        ProductDetail productDetail = product.getProductDetail();
        return ResponseEntity.ok(productDetailMapper.modelToDto(productDetail, fileHelper));
    }
}