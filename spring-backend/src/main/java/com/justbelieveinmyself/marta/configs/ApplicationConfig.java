package com.justbelieveinmyself.marta.configs;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ApplicationConfig {
    @Bean
    public FileHelper getFileHelper(){
        return new FileHelper();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
