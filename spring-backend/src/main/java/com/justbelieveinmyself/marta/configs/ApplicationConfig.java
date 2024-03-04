package com.justbelieveinmyself.marta.configs;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import com.justbelieveinmyself.marta.configs.beans.ProductHelper;
import com.justbelieveinmyself.marta.configs.beans.UserRightsValidator;
import com.justbelieveinmyself.marta.domain.mappers.ProductMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class ApplicationConfig {
    @Bean
    public FileHelper getFileHelper(){
        return new FileHelper();
    }

    @Bean
    public ProductHelper getProductHelper(ProductMapper productMapper) {
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

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.US);
        return slr;
    }
}
