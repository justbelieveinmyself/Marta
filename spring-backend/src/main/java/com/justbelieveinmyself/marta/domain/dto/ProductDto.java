package com.justbelieveinmyself.marta.domain.dto;

import com.justbelieveinmyself.marta.domain.entities.Product;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDto {
    public Product product;
    public String file;
}
