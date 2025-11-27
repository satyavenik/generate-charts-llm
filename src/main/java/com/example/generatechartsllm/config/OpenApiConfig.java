package com.example.generatechartsllm.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Chart Generation LLM API")
                        .version("1.0")
                        .description("REST API that accepts JSON data, uses LLM to analyze and recommend appropriate charts, then generates and returns chart images")
                        .contact(new Contact()
                                .name("API Support")
                                .email("support@example.com")));
    }
}

