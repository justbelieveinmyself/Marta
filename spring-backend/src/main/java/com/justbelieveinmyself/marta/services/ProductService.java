package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import com.justbelieveinmyself.marta.domain.dto.ProductDto;
import com.justbelieveinmyself.marta.domain.dto.ProductWithImageDto;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.enums.UploadDirectory;
import com.justbelieveinmyself.marta.exceptions.ForbiddenException;
import com.justbelieveinmyself.marta.exceptions.NotFoundException;
import com.justbelieveinmyself.marta.exceptions.ResponseMessage;
import com.justbelieveinmyself.marta.repositories.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FileHelper fileHelper;

    public ResponseEntity<?> getListProducts() {
        List<Product> products = productRepository.findAll();

        List<ProductWithImageDto> productWithImageDtoList = products.stream()
                .map(pro -> new ProductWithImageDto(ProductDto.of(pro), Base64.getEncoder().encodeToString(
                        fileHelper.downloadFileAsByteArray(pro.getPreviewImg(), UploadDirectory.PRODUCTS))))
                .toList();
        return ResponseEntity.ok(productWithImageDtoList);
    }

    public ResponseEntity<?> createProduct(ProductDto productDto, MultipartFile previewImage, User currentUser) {
        validateRights(productDto, currentUser);
        String imagePath = fileHelper.uploadFile(previewImage, UploadDirectory.PRODUCTS);
        Product product = Product.of(productDto);
        product.setPreviewImg(imagePath);
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.ok(ProductDto.of(savedProduct));
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
        return ResponseEntity.ok(ProductDto.of(productRepository.save(productFromDb)));
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
                .map(pro -> new ProductWithImageDto(ProductDto.of(pro), Base64.getEncoder().encodeToString(
                        fileHelper.downloadFileAsByteArray(pro.getPreviewImg(), UploadDirectory.PRODUCTS)))).findAny().get();
        return ResponseEntity.ok(productWithImageDtoList);
    }
}