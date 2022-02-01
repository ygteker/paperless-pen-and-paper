package lmu.msp.backend.service.implementation

import lmu.msp.backend.model.Campaign
import lmu.msp.backend.model.User
import lmu.msp.backend.repository.MemberRepository
import lmu.msp.backend.repository.UserRepository
import lmu.msp.backend.service.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * implementation of IUserService
 * contains the "business logic"
 *
 * @property userRepository
 * @property memberRepository
 */
@Service
class UserService(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val memberRepository: MemberRepository
) : IUserService {


    override fun getUserById(id: Long): User? {
        val user = userRepository.findById(id)
        return if (user.isEmpty) {
            null
        } else {
            user.get()
        }
    }

    override fun getUserByAuth0Id(authO: String): User {
        var user = userRepository.findUserByAuth0Id(authO)
        if (user == null) {
            user = userRepository.save(User(authO))
        }
        return user
    }

    @Transactional
    override fun removeCampaignFromUser(user: User, campaign: Campaign): User {
        if (campaign.owner.id == user.id) {
            campaign.campaignMember.forEach { it.user.campaignMember.remove(it) }
            campaign.campaignMember.clear()
            memberRepository.deleteByCampaignId(campaign.id)

            user.campaignOwner.removeIf { it.id == campaign.id }
        } else {
            val campaignMember = user.campaignMember.find { it.campaign.id == campaign.id }
            if (campaignMember != null) {
                user.campaignMember.remove(campaignMember)
                campaignMember.campaign.campaignMember.remove(campaignMember)

                memberRepository.save(campaignMember)
            }
        }
        return userRepository.save(user)
    }

    override fun updateProfileImage(authO: String, byteArray: ByteArray): Boolean {
        val user = getUserByAuth0Id(authO)
        user.image = byteArray

        userRepository.save(user)
        return true
    }

    override fun getProfileImage(authO: String, userId: Long): ByteArray? {
        val requestUser =  getUserByAuth0Id(authO)
        if (userId == requestUser.id) return requestUser.image
        val user = getUserById(userId) ?: return null
        return user.image
    }
}