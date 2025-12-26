package com.retailx.user_authentication_service;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(BuildProperties buildProperties) {
        return new OpenAPI()
                .info(new Info()
                        .title("User Authentication Service")
                        .version(buildProperties.getVersion())
                        .description("API documentation for the E-Commerce application")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}

