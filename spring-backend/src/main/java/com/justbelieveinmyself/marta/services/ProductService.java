package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import com.justbelieveinmyself.marta.domain.dto.ProductDto;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.domain.entities.User;
import com.justbelieveinmyself.marta.domain.enums.UploadTo;
import com.justbelieveinmyself.marta.exceptions.AppError;
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

        List<ProductDto> productDtoList = products.stream()
                .map(pro -> new ProductDto(pro, Base64.getEncoder().encodeToString(
                        fileHelper.downloadFileAsByteArray(pro.getPreviewImg(), UploadTo.PRODUCTS))))
                .toList();
        return ResponseEntity.ok(productDtoList);
    }

    public ResponseEntity<?> saveProduct(Product product, MultipartFile previewImage, User currentUser) throws IOException {

        if(!isHasRights(product, currentUser)){
            return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(),
                    "You don't have the rights!"),
                    HttpStatus.FORBIDDEN);
        }

        product.setId(null);
        String imagePath = fileHelper.uploadFile(previewImage, UploadTo.PRODUCTS);
        product.setPreviewImg(imagePath);
        return ResponseEntity.ok(productRepository.save(product));
    }

    public ResponseEntity<?> deleteProduct(Product product, User currentUser) {
        if(!isHasRights(product, currentUser)){
            return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(),
                    "You don't have the rights!"),
                    HttpStatus.FORBIDDEN);
        }
        productRepository.delete(product);
        return ResponseEntity.ok(new ResponseMessage(200, "deleted"));
    }

    public ResponseEntity<?> updateProduct(Product productFromDb, Product product, User currentUser) {
        if(!isHasRights(productFromDb, currentUser)){
            return new ResponseEntity<>(new AppError(HttpStatus.FORBIDDEN.value(),
                    "You don't have the rights!"),
                    HttpStatus.FORBIDDEN);
        }
        BeanUtils.copyProperties(product, productFromDb, "id");
        return ResponseEntity.ok(productRepository.save(productFromDb));
    }

    private boolean isHasRights(Product product, User currentUser) {
        return product.getSeller().getId().equals(currentUser.getId());
    }
}