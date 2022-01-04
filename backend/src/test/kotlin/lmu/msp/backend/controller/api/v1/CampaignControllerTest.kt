package lmu.msp.backend.controller.api.v1

import lmu.msp.backend.model.Campaign
import lmu.msp.backend.model.User
import lmu.msp.backend.repository.CampaignRepository
import lmu.msp.backend.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.*
import org.junit.jupiter.params.shadow.com.univocity.parsers.conversions.Conversions.string
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@AutoConfigureMockMvc
@SpringBootTest
internal class CampaignControllerTest(@Autowired private val mockMvc: MockMvc) {

    private companion object {
        private const val path = "/api/v1/campaign"

        private lateinit var owner: User
        private lateinit var member: User
        private lateinit var noMember: User


        @JvmStatic
        @BeforeAll
        fun init(@Autowired userRepository: UserRepository) {
            owner = userRepository.save(User("owner"))
            member = userRepository.save(User("member"))
            noMember = userRepository.save(User("noMember"))


        }

        @JvmStatic
        @AfterAll
        fun tearDown(@Autowired userRepository: UserRepository) {
            userRepository.deleteAll()
        }

    }

    private lateinit var campaign: Campaign

    @BeforeEach
    fun initCampaign(@Autowired campaignRepository: CampaignRepository) {
        campaign = campaignRepository.save(Campaign(owner, "title"))
    }

    @AfterEach
    fun tearDown(@Autowired campaignRepository: CampaignRepository) {
        campaignRepository.deleteAll()
    }

    @Test
    @WithMockUser(username = "owner")
    fun getCampaignOwner() {
        mockMvc.perform(
            get(path).param("campaignId", campaign.id.toString())
        ).andExpect {
            status().isOk
            jsonPath("$.id", `is`(campaign.id.toInt()))
        }
    }

    @Test
    @WithMockUser(username = "member")
    fun getCampaignMember() {
        mockMvc.perform(
            get(path).param("campaignId", campaign.id.toString())
        ).andExpect {
            status().isOk
            jsonPath("$.id", `is`(campaign.id.toInt()))
        }
    }

    @Test
    @WithMockUser(username = "noMember")
    fun getCampaignNoMember() {
        mockMvc.perform(
            get(path).param("campaignId", campaign.id.toString())
        ).andExpect {
            status().isOk
            content().string("")
        }
    }

    @Test
    @WithMockUser(username = "owner")
    fun createCampaign() {
        val title = "new title"
        mockMvc.perform(
            post(path).content(title)
        ).andExpect {
            status().isOk
            jsonPath("$.title", `is`(title))
        }
    }

    @Test
    @WithMockUser(username = "owner")
    fun deleteCampaignOwner(@Autowired campaignRepository: CampaignRepository) {
        mockMvc.perform(
            delete(path).param("campaignId", campaign.id.toString())
        ).andExpect {
            status().isOk
            content().string("true")
        }
        assertThat(campaignRepository.findById(campaign.id)).isEmpty
    }

    @Test
    @WithMockUser(username = "member")
    fun deleteCampaignMember(@Autowired campaignRepository: CampaignRepository) {
        mockMvc.perform(
            delete(path).param("campaignId", campaign.id.toString())
        ).andExpect {
            status().isOk
            content().string("false")
        }
        assertThat(campaignRepository.findById(campaign.id)).isNotEmpty
    }

    @Test
    @WithMockUser(username = "noMember")
    fun deleteCampaignNoMember(@Autowired campaignRepository: CampaignRepository) {
        mockMvc.perform(
            delete(path).param("campaignId", campaign.id.toString())
        ).andExpect {
            status().isOk
            content().string("false")
        }
        assertThat(campaignRepository.findById(campaign.id)).isNotEmpty
    }
}