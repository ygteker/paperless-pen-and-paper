package lmu.msp.backend.service.implementation

import lmu.msp.backend.model.Campaign
import lmu.msp.backend.repository.CampaignRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
internal class UserServiceTest(@Autowired val userService: UserService) {

    @Test
    fun getUserByAuth0Id() {
        val auth0Id = "test"

        //we don't have user yet => a new user needs to be created
        val user1 = userService.getUserByAuth0Id(auth0Id)
        assertThat(user1).isNotNull
        assertThat(user1.auth0Id).isEqualTo(auth0Id)

        //load the auth0id again. The user must be the same as the first
        val user2 = userService.getUserByAuth0Id(auth0Id)
        assertThat(user1.auth0Id).isEqualTo(user2.auth0Id)

    }

    @Test
    fun removeCampaignFromUser(@Autowired campaignRepository: CampaignRepository) {
        val auth0Owner = "owner"
        val auth0Member = "member"


        var userOwner = userService.getUserByAuth0Id(auth0Owner)
        val userMember = userService.getUserByAuth0Id(auth0Member)

        val campaign = campaignRepository.save(Campaign(userOwner, "test"))

        userOwner = userService.getUserByAuth0Id(auth0Owner)

        //check if new campaign is owned by owner
        assertThat(userOwner.id).isEqualTo(campaign.owner.id)


        userOwner = userService.removeCampaignFromUser(userOwner, campaign)


        //check if returned obj doesn't contain campaign
        assertThat(userOwner.campaignOwner).isEmpty()
        //check if changes are written to the db
        assertThat(userService.getUserByAuth0Id(auth0Owner).campaignOwner).isEmpty()
        //check if there is really no campaign anymore
        assertThat(campaignRepository.findByIdOrNull(campaign.id)).isNull()

        TODO("Delete Member from Campaign not yet implemented")
    }


}