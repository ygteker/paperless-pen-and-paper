package lmu.msp.backend.service.implementation

import lmu.msp.backend.model.Campaign
import lmu.msp.backend.model.CampaignMember
import lmu.msp.backend.model.User
import lmu.msp.backend.repository.CampaignRepository
import lmu.msp.backend.repository.MemberRepository
import lmu.msp.backend.repository.UserRepository
import lmu.msp.backend.service.IUserService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.web.WebAppConfiguration
import javax.persistence.EntityManager

@SpringBootTest
@WebAppConfiguration
internal class UserServiceTest(
    @Autowired private val userService: IUserService,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val campaignRepository: CampaignRepository,
    @Autowired private val memberRepository: MemberRepository
) {

    private val auth0Owner = "owner"
    private val auth0Member = "member"

    private var ownerId = 0L
    private var memberId = 0L

    private var campaignId: Long = 0


    @BeforeEach
    fun setUp() {
        val owner = userRepository.save(User(auth0Owner))
        val member = userRepository.save(User(auth0Member))
        val campaign = campaignRepository.save(Campaign(owner, "name"))

        val campaignMember = CampaignMember(campaign, member, "charName")
        campaign.campaignMember.add(campaignMember)
        member.campaignMember.add(campaignMember)
        memberRepository.save(campaignMember)

        campaignId = campaign.id

        ownerId = owner.id
        memberId = member.id
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
    fun getUserByAuth0Id() {
        val user = userService.getUserByAuth0Id(auth0Owner)
        assertThat(user.id).isEqualTo(userRepository.findUserByAuth0Id(auth0Owner)!!.id)
    }

    @Test
    fun getNewUserBy() {
        val auth0Id = "test"
        val user = userService.getUserByAuth0Id(auth0Id)
        assertThat(user.id).isEqualTo(userRepository.findUserByAuth0Id(auth0Id)!!.id)

        //load the auth0id again. The user must be the same as the first
        assertThat(userService.getUserByAuth0Id(auth0Id).id).isEqualTo(user.id)
    }

    @Test
    fun removeCampaignFromMember(@Autowired campaignRepository: CampaignRepository) {
        val member = userRepository.findUserByAuth0Id(auth0Member)!!
        val campaign = campaignRepository.findById(campaignId).get()
        userService.removeCampaignFromUser(member, campaign)

        assertThat(userRepository.findUserByAuth0Id(auth0Member)!!.campaignMember).isEmpty()
        assertThat(campaignRepository.findById(campaignId).get().campaignMember).isEmpty()
    }

    @Test
    fun removeCampaignFromOwner(@Autowired campaignRepository: CampaignRepository) {
        val owner = userRepository.findUserByAuth0Id(auth0Owner)!!
        val campaign = campaignRepository.findById(campaignId).get()
        userService.removeCampaignFromUser(owner, campaign)

        assertThat(campaignRepository.findById(campaignId)).isEmpty

        assertThat(userRepository.findUserByAuth0Id(auth0Owner)!!.campaignOwner).isEmpty()
    }

    @Test
    fun updateProfileImage() {
        assertThat(userService.updateProfileImage(auth0Owner, ByteArray(10)))
        assertThat(userRepository.findUserByAuth0Id(auth0Owner)!!.image.size).isEqualTo(10)
    }

    @Test
    fun getProfileImage() {
        assertThat(userService.getProfileImage(auth0Owner, ownerId)).isEmpty()


        val member = userRepository.findUserByAuth0Id(auth0Member)!!
        member.image = ByteArray(10)
        userRepository.save(member)

        assertThat(userService.getProfileImage(auth0Owner, memberId)!!.size).isEqualTo(10)
        assertThat(userService.getProfileImage(auth0Owner, -1)).isNull()

    }


}