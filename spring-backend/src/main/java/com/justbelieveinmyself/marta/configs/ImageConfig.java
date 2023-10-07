package com.justbelieveinmyself.marta.configs;

import com.justbelieveinmyself.marta.configs.beans.FileHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class ImageConfig {
    @Bean
    public FileHelper getFileHelper(){
        return new FileHelper();
    }
}
