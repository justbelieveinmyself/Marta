package com.justbelieveinmyself.marta.services;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import com.justbelieveinmyself.marta.domain.UploadTo;
import com.justbelieveinmyself.marta.domain.entities.Product;
import com.justbelieveinmyself.marta.repositories.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FileHelper fileHelper;

    public List<Product> getListProducts() {
        return productRepository.findAll();
    }

    public Product saveProduct(Product product, MultipartFile previewImage) throws IOException {
        String imagePath = fileHelper.uploadFile(previewImage, UploadTo.PRODUCTS);
        product.setPreviewImg(imagePath);
        return productRepository.save(product);
    }

    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    public Product updateProduct(Product productFromDb, Product product) {
        BeanUtils.copyProperties(product, productFromDb, "id");
        return productRepository.save(productFromDb);
    }
}
