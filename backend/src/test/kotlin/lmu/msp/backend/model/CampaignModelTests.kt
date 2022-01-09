package lmu.msp.backend.model

import lmu.msp.backend.repository.CampaignRepository
import lmu.msp.backend.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.InvalidDataAccessApiUsageException

@DataJpaTest
internal class CampaignModelTests(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val campaignRepository: CampaignRepository

) {

    @Test
    fun injectedComponentsAreNotNull() {
        assertThat(userRepository).isNotNull
        assertThat(campaignRepository).isNotNull
    }

    @Test
    fun createOwnerBeforeCampaignTest() {
        val u1 = User("u1")
        val c1 = Campaign(u1, "c1")
        u1.campaignOwner.add(c1)

        assertThrows<InvalidDataAccessApiUsageException> { campaignRepository.save(c1) }
    }

    @Test
    fun campaignUpdatesExistingOwnerTest() {

        userRepository.save(User("u1"))

        val u1 = userRepository.findUserByAuth0Id("u1")!!

        assertThat(u1.campaignOwner).isEmpty()

        //create campaign
        campaignRepository.save(Campaign(u1, "c1"))

        assertThat(userRepository.findUserByAuth0Id("u1")!!.campaignOwner.first().title).isEqualTo("c1")
        assertThat(campaignRepository.findAll().first().owner.auth0Id).isEqualTo("u1")
    }

    @Test
    fun deleteCampaignButNotOwnerTest() {

        userRepository.save(User("u1"))
        val u1 = userRepository.findUserByAuth0Id("u1")!!
        val c1 = Campaign(u1, "c1")


        campaignRepository.save(c1)

        assertThat(userRepository.findAll()).isNotEmpty
        assertThat(campaignRepository.findAll()).isNotEmpty

        //delete campaign -> campaign still registered in a owner => dont delete
        campaignRepository.delete(c1)
        assertThat(userRepository.findAll()).isNotEmpty
        assertThat(campaignRepository.findAll()).isNotEmpty

        //delete campaign from owner
        u1.campaignOwner.clear()
        userRepository.save(u1)
        assertThat(userRepository.findAll()).isNotEmpty
        assertThat(campaignRepository.findAll()).isEmpty()
    }

    @Test
    fun deleteCampaignWhenUserIsDeletedTest() {
        userRepository.save(User("u1"))
        val u1 = userRepository.findUserByAuth0Id("u1")!!
        val c1 = Campaign(u1, "c1")

        campaignRepository.save(c1)

        userRepository.delete(u1)
        assertThat(userRepository.findAll()).isEmpty()
        assertThat(campaignRepository.findAll()).isEmpty()
    }
}