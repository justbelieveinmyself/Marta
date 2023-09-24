package com.justbelieveinmyself.marta;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class MartaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MartaApplication.class, args);
	}

}
