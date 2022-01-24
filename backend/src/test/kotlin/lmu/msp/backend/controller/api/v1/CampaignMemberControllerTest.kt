package lmu.msp.backend.controller.api.v1

import lmu.msp.backend.model.Campaign
import lmu.msp.backend.model.CampaignMember
import lmu.msp.backend.model.User
import lmu.msp.backend.repository.CampaignRepository
import lmu.msp.backend.repository.MemberRepository
import lmu.msp.backend.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import javax.persistence.EntityManager

@AutoConfigureMockMvc
@SpringBootTest
@WebAppConfiguration
internal class CampaignMemberControllerTest(@Autowired private val mockMvc: MockMvc) {

    val charName = "charName"

    val auth0Owner = "owner"
    val auth0Member = "member"
    val auth0NoMember = "nomember"

    val campaignName = "name"

    var campaignId: Long = 0

    @BeforeEach
    fun initTest(
        @Autowired campaignRepository: CampaignRepository,
        @Autowired userRepository: UserRepository,
        @Autowired memberRepository: MemberRepository
    ) {
        val owner = userRepository.save(User(auth0Owner))
        val member = userRepository.save(User(auth0Member))
        userRepository.save(User(auth0NoMember))

        val campaign = campaignRepository.save(Campaign(owner, campaignName))
        campaignId = campaign.id

        val campaignMember = CampaignMember(campaign, member, charName)
        campaign.campaignMember.add(campaignMember)
        member.campaignMember.add(campaignMember)

        memberRepository.save(campaignMember)

    }

    @AfterEach
    fun tearDown(
        @Autowired entityManager: EntityManager,
        @Autowired campaignRepository: CampaignRepository,
        @Autowired userRepository: UserRepository,
        @Autowired memberRepository: MemberRepository
    ) {
        entityManager.clear()
        userRepository.deleteAll()
        campaignRepository.deleteAll()
        memberRepository.deleteAll()
    }

    private val path = "/api/v1/campaign/member"


    @Test
    @WithMockUser(username = "owner")
    fun getMembersOwner() {
        mockMvc.perform(get(path).param("campaignId", campaignId.toString()))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].campaign.id").value(campaignId))
    }

    @Test
    @WithMockUser(username = "member")
    fun getMembersMember() {
        mockMvc.perform(get(path).param("campaignId", campaignId.toString()))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].campaign.id").value(campaignId))
    }

    @Test
    @WithMockUser(username = "noMember")
    fun getMembersNoMember() {
        mockMvc.perform(get(path).param("campaignId", campaignId.toString()))
            .andExpect(status().isNotFound)
            .andExpect(content().string(""))
    }

    @Test
    @WithMockUser(username = "owner")
    fun deleteMemberOwner(@Autowired userRepository: UserRepository) {
        val user = userRepository.findUserByAuth0Id(auth0Member)!!

        mockMvc.perform(
            delete(path)
                .param("campaignId", campaignId.toString()).param("userToRemoveId", user.id.toString())
                .with(csrf().asHeader())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isEmpty)

        assertThat(userRepository.findUserByAuth0Id(auth0Member)!!.campaignMember).isEmpty()

    }

    @Test
    @WithMockUser(username = "member")
    fun deleteMemberMember(@Autowired userRepository: UserRepository) {
        val user = userRepository.findUserByAuth0Id(auth0Member)!!

        mockMvc.perform(
            delete(path)
                .param("campaignId", campaignId.toString()).param("userToRemoveId", user.id.toString())
                .with(csrf().asHeader())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isEmpty)

        assertThat(userRepository.findUserByAuth0Id(auth0Member)!!.campaignMember).isEmpty()
    }

    @Test
    @WithMockUser(username = "noMember")
    fun deleteMemberNoMember(@Autowired userRepository: UserRepository) {
        val user = userRepository.findUserByAuth0Id(auth0Member)!!

        mockMvc.perform(
            delete(path)
                .param("campaignId", campaignId.toString()).param("userToRemoveId", user.id.toString())
                .with(csrf().asHeader())
        )
            .andExpect(status().isNotFound)
            .andExpect(content().string(""))

        assertThat(userRepository.findUserByAuth0Id(auth0Member)!!.campaignMember).isNotEmpty
    }

    @Test
    @WithMockUser(username = "owner")
    fun updateMemberNoMember(@Autowired userRepository: UserRepository) {
        mockMvc.perform(
            put(path)
                .param("campaignId", campaignId.toString()).param("name", "newName")
                .with(csrf().asHeader())
        )
            .andExpect(status().isNotFound)
            .andExpect(content().string(""))
    }

    @Test
    @WithMockUser(username = "member")
    fun updateMemberMember(@Autowired memberRepository: MemberRepository) {
        mockMvc.perform(
            put(path)
                .param("campaignId", campaignId.toString()).param("name", "newName")
                .with(csrf().asHeader())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.characterName").value("newName"))

        assertThat(
            memberRepository.findByCampaignIdAndUserAuth0Id(
                campaignId,
                auth0Member
            )!!.characterName
        ).isEqualTo("newName")
    }

    @Test
    @WithMockUser(username = "owner")
    fun acceptInviteOwner() {
        mockMvc.perform(
            post("$path/invite/accept")
                .param("campaignId", campaignId.toString()).param("name", "newName")
                .with(csrf().asHeader())
        )
            .andExpect(status().isNotFound)
            .andExpect(content().string(""))
    }

    @Test
    @WithMockUser(username = "noMember")
    fun acceptInviteNewMember() {
        mockMvc.perform(
            post("$path/invite/accept")
                .param("campaignId", campaignId.toString()).param("name", "newName")
                .with(csrf().asHeader())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.characterName").value("newName"))
            .andExpect(jsonPath("$.campaign.title").value(campaignName))
    }

    @Test
    @WithMockUser(username = "member")
    fun acceptInviteMember() {
        mockMvc.perform(
            post("$path/invite/accept")
                .param("campaignId", campaignId.toString()).param("name", "newName")
                .with(csrf().asHeader())
        )
            .andExpect(status().isNotFound)
            .andExpect(content().string(""))
    }
}