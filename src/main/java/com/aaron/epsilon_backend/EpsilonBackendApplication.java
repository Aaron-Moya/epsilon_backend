package com.aaron.epsilon_backend;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@SpringBootApplication
@OpenAPIDefinition
@Configuration
public class EpsilonBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EpsilonBackendApplication.class, args);
	}

    @Bean
    OpenAPI baseOpenAPI() {
        return new OpenAPI().info(new Info().title("Epsilon API REST Doc")
        		.version("1.0.0").description("Epsilon API REST Doc"));
    }
    
}	
