package lmu.msp.backend.model

import lmu.msp.backend.repository.CampaignRepository
import lmu.msp.backend.repository.MemberRepository
import lmu.msp.backend.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import javax.persistence.EntityManager

@DataJpaTest
internal class CampaignMemberTest(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val campaignRepository: CampaignRepository,
    @Autowired private val memberRepository: MemberRepository
) {

    private var memberId: Long = 0

    private var campaignId: Long = 0
    private var campaignMemberId: Long = 0

    @BeforeEach
    fun setUp() {
        val owner = userRepository.save(User("owner"))
        val member = userRepository.save(User("member"))
        val noMember = userRepository.save(User("noMember"))

        val campaign = campaignRepository.save(Campaign(owner, "name"))

        val campaignMember = CampaignMember(campaign, member, "charName")
        campaign.campaignMember.add(campaignMember)
        member.campaignMember.add(campaignMember)

        campaignMemberId = memberRepository.save(campaignMember).id

        memberId = member.id
        campaignId = campaign.id

    }

    @AfterEach
    fun tearDown(@Autowired entityManager: EntityManager) {
        entityManager.clear()
    }


    @Test
    fun addMember() {
        //member should already be added in setup => check if member present in campaign

        val campaign = campaignRepository.findById(campaignId).get()
        val member = userRepository.findById(memberId).get()

        val campaignMember = memberRepository.findByCampaignIdAndUserId(campaignId, memberId)!!

        assertThat(campaign.campaignMember).isNotEmpty
        assertThat(member.campaignMember).isNotEmpty
        assertThat(campaignMember.characterName).isEqualTo("charName")
        assertThat(campaign.campaignMember[0].id).isEqualTo(campaignMember.id)
        assertThat(member.campaignMember[0].id).isEqualTo(campaignMember.id)

    }

    @Test
    fun removeMember() {
        val campaignMember = memberRepository.findById(campaignMemberId).get()
        campaignMember.user.campaignMember.remove(campaignMember)
        campaignMember.campaign.campaignMember.remove(campaignMember)

        memberRepository.deleteById(campaignMemberId)


        assertThat(memberRepository.findByCampaignIdAndUserId(campaignId, memberId)).isNull()
        assertThat(memberRepository.findById(campaignMemberId)).isEmpty


        assertThat(campaignRepository.findById(campaignId)).isNotEmpty
        assertThat(campaignRepository.findById(campaignId).get().campaignMember).isEmpty()

        assertThat(userRepository.findById(memberId)).isNotEmpty
        assertThat(userRepository.findById(memberId).get().campaignMember).isEmpty()
    }

    @Test
    fun renameMember() {
        val campaignMember = memberRepository.findById(campaignMemberId).get()
        val newName = "newCharName"

        campaignMember.characterName = newName

        memberRepository.save(campaignMember)

        assertThat(memberRepository.findById(campaignMemberId).get().characterName).isEqualTo(newName)
        assertThat(userRepository.findById(memberId).get().campaignMember[0].characterName).isEqualTo(newName)
        assertThat(campaignRepository.findById(campaignId).get().campaignMember[0].characterName).isEqualTo(newName)

    }


}