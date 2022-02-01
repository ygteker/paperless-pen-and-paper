package lmu.msp.backend.configuration

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * basic configuration class for swagger
 * needed to enable the authentication in swagger with a bearer-key (lock symbol on the webpage to login)
 *
 */
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