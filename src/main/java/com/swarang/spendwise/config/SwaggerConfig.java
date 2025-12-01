package com.swarang.spendwise.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "SpendWise API",
                description = "Expense Tracker Rest API",
                version = "1.0.0"
        )
)
public class SwaggerConfig {

    @Bean
    public OpenApiCustomizer customizeOpenApi(){
        return openApi -> openApi
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .getComponents().addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"));
    }
}
