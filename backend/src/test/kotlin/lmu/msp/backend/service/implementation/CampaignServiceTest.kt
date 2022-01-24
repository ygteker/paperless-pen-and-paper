package lmu.msp.backend.service.implementation

import lmu.msp.backend.model.Campaign
import lmu.msp.backend.model.CampaignMember
import lmu.msp.backend.model.User
import lmu.msp.backend.repository.CampaignRepository
import lmu.msp.backend.repository.MemberRepository
import lmu.msp.backend.repository.UserRepository
import lmu.msp.backend.service.ICampaignService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.web.WebAppConfiguration
import javax.persistence.EntityManager

@SpringBootTest
@WebAppConfiguration
internal class CampaignServiceTest(@Autowired private val campaignService: ICampaignService) {

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
        val noMember = userRepository.save(User(auth0NoMember))

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

    @Test
    fun getCampaign() {
        assertThat(campaignService.getCampaign(auth0Owner, campaignId)).isNotNull
        assertThat(campaignService.getCampaign(auth0Member, campaignId)).isNotNull
        assertThat(campaignService.getCampaign(auth0NoMember, campaignId)).isNull()
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
    fun getMembersOwner() {
        val members = campaignService.getMembers(auth0Owner, campaignId)
        assertThat(members).isNotNull
        members!!
        assertThat(members.size).isEqualTo(1)
        assertThat(members[0].user.auth0Id).isEqualTo(auth0Member)
    }

    @Test
    fun getMembersMember() {
        val members = campaignService.getMembers(auth0Member, campaignId)
        assertThat(members).isNotNull
        members!!
        assertThat(members.size).isEqualTo(1)
        assertThat(members[0].user.auth0Id).isEqualTo(auth0Member)
    }

    @Test
    fun getMembersNoMember() {
        val members = campaignService.getMembers(auth0NoMember, campaignId)
        assertThat(members).isNull()
    }

    @Test
    fun addMemberOwner() {
        val campaign = campaignService.addMember(auth0Owner, campaignId, charName)
        assertThat(campaign).isNull()
    }

    @Test
    fun addMemberMember() {
        val campaign = campaignService.addMember(auth0Member, campaignId, charName)
        assertThat(campaign).isNull()
    }

    @Test
    fun addMemberNoMember(@Autowired userRepository: UserRepository) {
        val campaignMember = campaignService.addMember(auth0NoMember, campaignId, charName)
        assertThat(campaignMember).isNotNull
        campaignMember!!
        assertThat(campaignMember.campaign.campaignMember.size).isEqualTo(2)
        assertThat(userRepository.findUserByAuth0Id(auth0NoMember)!!.campaignMember.size).isEqualTo(1)
    }

    @Test
    fun removeNotMemberByOwner(@Autowired userRepository: UserRepository) {
        val userToRemove = userRepository.findUserByAuth0Id(auth0Owner)!!
        val campaign = campaignService.removeMember(auth0Owner, campaignId, userToRemove.id)
        assertThat(campaign).isNull()
    }

    @Test
    fun removeMemberOwner(
        @Autowired userRepository: UserRepository,
        @Autowired campaignRepository: CampaignRepository
    ) {
        val userToRemove = userRepository.findUserByAuth0Id(auth0Member)!!
        val campaign = campaignService.removeMember(auth0Owner, campaignId, userToRemove.id)

        assertThat(campaign).isNotNull

        assertThat(campaignRepository.findById(campaignId).get().campaignMember).isEmpty()
        assertThat(userRepository.findUserByAuth0Id(auth0Member)!!.campaignMember).isEmpty()
    }

    @Test
    fun removeMemberMember(
        @Autowired userRepository: UserRepository,
        @Autowired campaignRepository: CampaignRepository
    ) {
        val userToRemove = userRepository.findUserByAuth0Id(auth0Member)!!
        val campaign = campaignService.removeMember(auth0Owner, campaignId, userToRemove.id)

        assertThat(campaign).isNotNull

        assertThat(campaignRepository.findById(campaignId).get().campaignMember).isEmpty()
        assertThat(userRepository.findUserByAuth0Id(auth0Member)!!.campaignMember).isEmpty()
    }

    @Test
    fun removeMemberNoMember(
        @Autowired userRepository: UserRepository,
        @Autowired campaignRepository: CampaignRepository
    ) {
        val userToRemove = userRepository.findUserByAuth0Id(auth0NoMember)!!
        val campaign = campaignService.removeMember(auth0Owner, campaignId, userToRemove.id)

        assertThat(campaign).isNull()

        assertThat(campaignRepository.findById(campaignId).get().campaignMember).isNotEmpty
        assertThat(userRepository.findUserByAuth0Id(auth0Member)!!.campaignMember).isNotEmpty
    }

    @Test
    fun renameMember(@Autowired memberRepository: MemberRepository) {
        val newName = "newName"
        val campaign = campaignService.renameMember(auth0Member, campaignId, newName)

        assertThat(campaign).isNotNull

        assertThat(memberRepository.findByCampaignIdAndUserAuth0Id(campaignId, auth0Member)!!.characterName).isEqualTo(
            newName
        )

    }
}