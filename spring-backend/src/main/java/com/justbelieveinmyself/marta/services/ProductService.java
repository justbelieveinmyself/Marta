package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import com.justbelieveinmyself.marta.domain.dto.ProductDto;
import com.justbelieveinmyself.marta.domain.dto.ProductWithImageDto;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.enums.UploadDirectory;
import com.justbelieveinmyself.marta.exceptions.ResponseError;
import com.justbelieveinmyself.marta.exceptions.ResponseMessage;
import com.justbelieveinmyself.marta.repositories.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

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

    public ResponseEntity<?> createProduct(ProductDto productDto, MultipartFile previewImage, User currentUser) throws IOException {

        if(!isHasRights(productDto, currentUser)){
            return new ResponseEntity<>(new ResponseError(HttpStatus.FORBIDDEN.value(),
                    "You don't have the rights!"),
                    HttpStatus.FORBIDDEN);
        }
        String imagePath = fileHelper.uploadFile(previewImage, UploadDirectory.PRODUCTS);
        Product product = Product.of(productDto);
        product.setPreviewImg(imagePath);
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.ok(ProductDto.of(savedProduct));
    }

    public ResponseEntity<?> deleteProduct(Product product, User currentUser) {
        if(!isHasRights(product, currentUser)){
            return new ResponseEntity<>(new ResponseError(HttpStatus.FORBIDDEN.value(),
                    "You don't have the rights!"),
                    HttpStatus.FORBIDDEN);
        }
        productRepository.delete(product);
        return ResponseEntity.ok(new ResponseMessage(200, "deleted"));
    }

    public ResponseEntity<?> updateProduct(Product productFromDb, ProductDto productDto, User currentUser) {
        if(!isHasRights(productFromDb, currentUser)){
            return new ResponseEntity<>(new ResponseError(HttpStatus.FORBIDDEN.value(),
                    "You don't have the rights!"),
                    HttpStatus.FORBIDDEN);
        }
        BeanUtils.copyProperties(productDto, productFromDb, "id", "seller");
        return ResponseEntity.ok(ProductDto.of(productRepository.save(productFromDb)));
    }

    private boolean isHasRights(Product product, User currentUser) {
        return product.getSeller().getId().equals(currentUser.getId());
    }
    private boolean isHasRights(ProductDto product, User currentUser) {
        return product.getSeller().getId().equals(currentUser.getId());
    }
}