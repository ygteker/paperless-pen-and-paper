package lmu.msp.backend.configuration

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

/**
 * basic configuration class for the jpa
 * tells the jpa where to find the repositories and the models/entities
 *
 */
@Configuration
@EnableJpaRepositories("lmu.msp.backend.repository")
@EntityScan("lmu.msp.backend.model")
class JpaRepositoryConfiguration {
}