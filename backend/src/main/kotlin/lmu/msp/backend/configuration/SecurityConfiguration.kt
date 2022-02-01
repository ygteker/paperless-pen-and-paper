package lmu.msp.backend.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult
import org.springframework.security.oauth2.jwt.*


/**
 * restrict access to our endpoints
 */
@EnableWebSecurity
class SecurityConfiguration : WebSecurityConfigurerAdapter() {
    @Value("\${auth0.audience}")
    private val audience: String? = null

    @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private val issuer: String? = null

    @Throws(Exception::class)
    public override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
            .antMatchers(
                //allow demo page
                "/hello-world",
                //allow swagger 3
                "/api-docs",
                "/v3/api-docs/**",
                "/swagger-ui/**"
            ).permitAll()
            .anyRequest().authenticated()
            .and().cors()
            .and().oauth2ResourceServer().jwt()
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        /*
        By default, Spring Security does not validate the "aud" claim of the token, to ensure that this token is
        indeed intended for our app. Adding our own validator is easy to do:
        */
        val jwtDecoder = JwtDecoders.fromOidcIssuerLocation<JwtDecoder>(issuer) as NimbusJwtDecoder
        val audienceValidator: OAuth2TokenValidator<Jwt> = AudienceValidator(
            audience!!
        )
        val withIssuer = JwtValidators.createDefaultWithIssuer(issuer)
        val withAudience: OAuth2TokenValidator<Jwt> = DelegatingOAuth2TokenValidator(withIssuer, audienceValidator)
        jwtDecoder.setJwtValidator(withAudience)
        return jwtDecoder
    }

    /**
     * Validates that the JWT token contains the intended audience in its claims.
     * implementation similar to the auth0 example https://auth0.com/docs/quickstart/backend/java-spring-security5
     */
    internal class AudienceValidator(private val audience: String) : OAuth2TokenValidator<Jwt> {

        override fun validate(jwt: Jwt): OAuth2TokenValidatorResult {
            return if (jwt.audience.contains(audience)) {
                OAuth2TokenValidatorResult.success()
            } else {
                val error = OAuth2Error("invalid_token", "The required audience is missing", null)
                OAuth2TokenValidatorResult.failure(error)
            }
        }
    }
}
