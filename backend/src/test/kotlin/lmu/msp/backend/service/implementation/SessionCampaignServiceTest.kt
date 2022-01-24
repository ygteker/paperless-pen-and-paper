package lmu.msp.backend.service.implementation

import lmu.msp.backend.service.ISessionCampaignService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.web.WebAppConfiguration

@SpringBootTest
@WebAppConfiguration
internal class SessionCampaignServiceTest(@Autowired private val sessionCampaignService: ISessionCampaignService) {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun handleMessage() {
    }

    @Test
    fun connected() {
    }

    @Test
    fun disconnected() {
    }
}