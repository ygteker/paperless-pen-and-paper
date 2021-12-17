package lmu.msp.backend.security

import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult
import org.springframework.security.oauth2.jwt.Jwt

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