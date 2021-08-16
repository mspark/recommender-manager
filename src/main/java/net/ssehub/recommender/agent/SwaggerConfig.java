package net.ssehub.recommender.agent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Provides OpenAPI / Swagger configuration per bean definition.
 * 
 * @author marcel
 */
@Configuration
public class SwaggerConfig {
    
    /**
     * Open API Definition. Sets authentication mechanism, version and the title.
     * 
     * @param appVersion
     * @param title
     * @return Configured OpenAPI configuration
     */
    @Bean
    public OpenAPI customOpenAPI(@Value("${urapi.version}") String appVersion, @Value("${urapi.title}") String title) {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearer-key",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new Info().title(title).version("URAPI: " + appVersion)
                        .description("Based on the unified recommender api")
                        .license(new License().name("Apache 2.0").url("")));
    }
}