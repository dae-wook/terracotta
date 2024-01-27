package com.daesoo.terracotta.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
    .info(new Info()
    .title("terracotta API")
    .description("teracotta API docs.")
    .version("1.0.0"))
    .components(new Components()
            .addSecuritySchemes("bearer-key",
                new io.swagger.v3.oas.models.security.SecurityScheme()
                .type(Type.HTTP)
                .scheme("Bearer")
                .bearerFormat("JWT")));
  }
}
