package lmu.msp.backend.controller.api.v1

import lmu.msp.backend.model.Campaign
import lmu.msp.backend.model.User
import lmu.msp.backend.repository.CampaignRepository
import lmu.msp.backend.repository.UserRepository
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest
internal class CampaignMemberControllerTest(@Autowired private val mockMvc: MockMvc) {

    private companion object {
        private const val path = "/api/v1/campaign/member"

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
    fun getMembersOwner() {
        mockMvc.perform(get(path).param("campaignId", campaign.id.toString()))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(campaign.id))
        TODO("Not yet implemented")
    }

    @Test
    @WithMockUser(username = "member")
    fun getMembersMember() {
        TODO("Not yet implemented")
    }

    @Test
    @WithMockUser(username = "noMember")
    fun getMembersNoMember() {
        TODO("Not yet implemented")
    }

    @Test
    @WithMockUser(username = "owner")
    fun deleteMemberOwner() {
        TODO("Not yet implemented")
    }

    @Test
    @WithMockUser(username = "member")
    fun deleteMemberMember() {
        TODO("Not yet implemented")
    }

    @Test
    @WithMockUser(username = "noMember")
    fun deleteMemberNoMember() {
        TODO("Not yet implemented")
    }

    @Test
    fun updateMember() {
        TODO("Not yet implemented")
    }

    @Test
    fun acceptInvite() {
        TODO("Not yet implemented")
    }

    @Test
    fun getInviteLink() {
        TODO("Not yet implemented")
    }
}