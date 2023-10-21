package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import com.justbelieveinmyself.marta.domain.dto.ProductDto;
import com.justbelieveinmyself.marta.domain.dto.ProductWithImageDto;
import com.justbelieveinmyself.marta.domain.dto.ReviewDto;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.entities.Review;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.enums.UploadDirectory;
import com.justbelieveinmyself.marta.domain.mappers.ProductMapper;
import com.justbelieveinmyself.marta.exceptions.ForbiddenException;
import com.justbelieveinmyself.marta.exceptions.NotFoundException;
import com.justbelieveinmyself.marta.exceptions.ResponseMessage;
import com.justbelieveinmyself.marta.repositories.ProductRepository;
import com.justbelieveinmyself.marta.repositories.ReviewRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Stream;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ReviewRepository reviewRepository;
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
        if (Objects.isNull(product))
            throw new NotFoundException("Product with [id] doesn't exists");
        validateRights(product, currentUser);
        productRepository.delete(product);
        return ResponseEntity.ok(new ResponseMessage(200, "deleted"));
    }

    public ResponseEntity<?> updateProduct(Product productFromDb, ProductDto productDto, User currentUser) {
        if (Objects.isNull(productFromDb)) {
            throw new NotFoundException("Product with [id] doesn't exists");
        }
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
        if (Objects.isNull(product))
            throw new NotFoundException("Product with [id] doesn't exists");
        Stream<Product> productStream = Stream.of(product);
        ProductWithImageDto productWithImageDtoList = productStream
                .map(pro -> new ProductWithImageDto(productMapper.modelToDto(pro), Base64.getEncoder().encodeToString(
                        fileHelper.downloadFileAsByteArray(pro.getPreviewImg(), UploadDirectory.PRODUCTS)))).findAny().get();
        return ResponseEntity.ok(productWithImageDtoList);
    }

    public ResponseEntity<?> getListProductReviews(Product product) {
        if (Objects.isNull(product))
            throw new NotFoundException("Product with [id] doesn't exists");
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
            throw new NotFoundException("Product with [id] doesn't exists");
        }
        List<String> uploadPaths = fileHelper.uploadFile(photos, UploadDirectory.REVIEWS);
        Product product = productOpt.get();
        Review review = new Review();
        review.setTime(ZonedDateTime.now());
        review.setMessage(reviewDto.getMessage());
        review.setAnswer(reviewDto.getAnswer());
        review.setRating(reviewDto.getRating());
        review.setPhotos(uploadPaths);
        review.setProduct(product);
        review.setAuthor(author);
        Review savedReview = reviewRepository.save(review);
        product.getReviews().add(savedReview);
        productRepository.save(product);
        List<String> base64photos = review.getPhotos().stream().map(photo
                -> Base64.getEncoder().encodeToString(fileHelper.downloadFileAsByteArray(photo, UploadDirectory.REVIEWS))).toList();
        review.setPhotos(base64photos);
        return ResponseEntity.ok(ReviewDto.of(review));
    }

    public ResponseEntity<?> deleteProductReview(Review review) {
        if (Objects.isNull(review))
            throw new NotFoundException("Review with [id] doesn't exists");
        //no need to validate rights cause can be deleted only with authority "admin"
        reviewRepository.delete(review);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK.value(), "Successfully deleted"));
    }
}