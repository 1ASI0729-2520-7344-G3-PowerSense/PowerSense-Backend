package com.powersense.shared.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI powersenseAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PowerSense API")
                        .description("Documentación de la API para PowerSense Backend")
                        .version("1.0.0"));
    }
}


