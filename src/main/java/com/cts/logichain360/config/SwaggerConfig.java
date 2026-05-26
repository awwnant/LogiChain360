package com.cts.logichain360.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Logistics And Supply Chain Management")
                        .version("1.0")
                        .description("Proto")
                        .contact(new Contact().name("Anant").email("Anant.Mishra@cognizant.com")));
    }
}