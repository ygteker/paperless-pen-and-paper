package lmu.msp.backend.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.jwt.*


/**
 * Configures our application with Spring Security to restrict access to our API endpoints.
 */
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {
    @Value("\${auth0.audience}")
    private val audience: String? = null

    @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private val issuer: String? = null

    @Throws(Exception::class)
    public override fun configure(http: HttpSecurity) {
        /*
        configure security for the endpoints
        */
        http
            .authorizeRequests()
            .antMatchers(
                //allow demo page
                "/hello-world",
                //allow swagger 3
                "/api-docs",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/h2-console",
                "/h2-console/**"
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
}
