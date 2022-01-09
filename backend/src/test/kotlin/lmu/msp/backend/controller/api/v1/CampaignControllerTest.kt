package lmu.msp.backend.controller.api.v1

import lmu.msp.backend.model.Campaign
import lmu.msp.backend.model.User
import lmu.msp.backend.repository.CampaignRepository
import lmu.msp.backend.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


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

    private var campaignId: Long = 0

    @BeforeEach
    fun initCampaign(@Autowired campaignRepository: CampaignRepository) {
        campaignId = campaignRepository.save(Campaign(owner, "title")).id
    }

    @AfterEach
    fun tearDown(@Autowired campaignRepository: CampaignRepository) {
        campaignRepository.deleteAll()
    }

    @Test
    @WithMockUser(username = "owner")
    fun getCampaignOwner() {
        mockMvc.perform(get(path).param("campaignId", campaignId.toString()))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(campaignId))
    }

    @Test
    @WithMockUser(username = "member")
    fun getCampaignMember() {
        mockMvc.perform(get(path).param("campaignId", campaignId.toString()))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(campaignId))
        TODO("campaing member not yet implemented in testing")
    }

    @Test
    @WithMockUser(username = "noMember")
    fun getCampaignNoMember() {
        mockMvc.perform(get(path).param("campaignId", campaignId.toString()))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").doesNotExist())
    }

    @Test
    @WithMockUser(username = "owner")
    fun createCampaign() {
        val title = "new title"
        mockMvc.perform(post(path).content(title).with(csrf().asHeader()))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value(title))
    }

    @Test
    @WithMockUser(username = "owner")
    fun deleteCampaignOwner(@Autowired campaignRepository: CampaignRepository) {
        mockMvc.perform(
            delete(path).param("campaignId", campaignId.toString()).with(csrf().asHeader())
        )
            .andExpect(status().isOk)
            .andExpect(content().string("true"))

        assertThat(campaignRepository.findById(campaignId)).isEmpty
    }

    @Test
    @WithMockUser(username = "member")
    fun deleteCampaignMember(@Autowired campaignRepository: CampaignRepository) {
        mockMvc.perform(
            delete(path).param("campaignId", campaignId.toString()).with(csrf().asHeader())
        )
            .andExpect(status().isOk)
            .andExpect(content().string("false"))
        assertThat(campaignRepository.findById(campaignId)).isNotEmpty
    }

    @Test
    @WithMockUser(username = "noMember")
    fun deleteCampaignNoMember(@Autowired campaignRepository: CampaignRepository) {
        mockMvc.perform(
            delete(path).param("campaignId", campaignId.toString()).with(csrf().asHeader())
        )
            .andExpect(status().isOk)
            .andExpect(content().string("false"))
        assertThat(campaignRepository.findById(campaignId)).isNotEmpty
    }
}