package com.justbelieveinmyself.marta.configs;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import com.justbelieveinmyself.marta.configs.beans.ProductHelper;
import com.justbelieveinmyself.marta.configs.beans.UserRightsValidator;
import com.justbelieveinmyself.marta.domain.mappers.ProductMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ApplicationConfig {
    private final ProductMapper productMapper;

    public ApplicationConfig(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Bean
    public FileHelper getFileHelper(){
        return new FileHelper();
    }
    @Bean
    public ProductHelper getProductHelper() {
        return new ProductHelper(getFileHelper(), productMapper);
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserRightsValidator userRightsValidator(){
        return new UserRightsValidator();
    }
}
