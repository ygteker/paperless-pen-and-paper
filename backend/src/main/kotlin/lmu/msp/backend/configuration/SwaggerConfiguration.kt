package lmu.msp.backend.configuration

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SwaggerConfiguration {
    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI().components(
            Components().addSecuritySchemes(
                "bearer-key", SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
            )
        )
    }

}