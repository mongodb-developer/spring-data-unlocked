package com.mongodb.application.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Customer API")
                .version("1.0")
                .description("API for managing customers in MongoDB"));
    }

    @Bean
    public GroupedOpenApi publicCustomerApi() {
        return GroupedOpenApi.builder()
            .group("customers")
            .pathsToMatch("/customers/**")
            .build();
    }
}
