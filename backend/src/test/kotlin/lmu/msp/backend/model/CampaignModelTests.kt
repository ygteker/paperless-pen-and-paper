package lmu.msp.backend.model

import lmu.msp.backend.repository.CampaignRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class CampaignModelTests(
    @Autowired private val campaignRepository: CampaignRepository

) {

    @Test
    fun injectedComponentsAreNotNull() {
        Assertions.assertThat(campaignRepository).isNotNull
    }
}