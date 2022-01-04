package lmu.msp.backend.service.implementation

import lmu.msp.backend.model.Campaign
import lmu.msp.backend.model.User
import lmu.msp.backend.repository.CampaignRepository
import lmu.msp.backend.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class CampaignServiceTest(@Autowired private val campaignService: CampaignService) {

    val auth0Owner = "owner"
    val auth0Member = "member"
    val auth0NoMember = "nomember"

    val campaignName = "name"

    var campaignId: Long = 0

    @BeforeEach
    fun initTest(@Autowired campaignRepository: CampaignRepository, @Autowired userRepository: UserRepository) {
        val owner = userRepository.save(User(auth0Owner))
        val member = userRepository.save(User(auth0Member))
        val noMember = userRepository.save(User(auth0NoMember))

        val campaign = campaignRepository.save(Campaign(owner, campaignName))
        campaignId = campaign.id
        println(campaignId)

    }

    @AfterEach
    fun tearDown(@Autowired campaignRepository: CampaignRepository, @Autowired userRepository: UserRepository) {
        userRepository.deleteAll()
        campaignRepository.deleteAll()
    }

    @Test
    fun getCampaign() {
        assertThat(campaignService.getCampaign(auth0Owner, campaignId)).isNotNull
        //assertThat(campaignService.getCampaign(auth0Member,campaignId)).isNotNull
        assertThat(campaignService.getCampaign(auth0NoMember, campaignId)).isNull()

        TODO("Member from Campaign not yet implemented")
    }

    @Test
    fun createCampaign(@Autowired campaignRepository: CampaignRepository) {
        val title = "test2"
        val campaign = campaignService.createCampaign(auth0Owner, title)

        val repoCampaign = campaignRepository.findById(campaign.id)

        assertThat(repoCampaign).isNotNull
        assertThat(repoCampaign.get().owner.auth0Id).isEqualTo(auth0Owner)
        assertThat(repoCampaign.get().title).isEqualTo(title)
    }

    @Test
    fun deleteCampaign() {
        assertFalse(campaignService.deleteCampaign(auth0NoMember, campaignId))
        assertFalse(campaignService.deleteCampaign(auth0Member, campaignId))
        assertTrue(campaignService.deleteCampaign(auth0Owner, campaignId))
    }

    @Test
    fun getMembers() {
    }

    @Test
    fun inviteMember() {
    }

    @Test
    fun acceptMember() {
    }

    @Test
    fun removeMember() {
    }
}