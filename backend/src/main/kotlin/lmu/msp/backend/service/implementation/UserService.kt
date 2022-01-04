package lmu.msp.backend.service.implementation

import lmu.msp.backend.model.Campaign
import lmu.msp.backend.model.User
import lmu.msp.backend.repository.UserRepository
import lmu.msp.backend.service.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(@Autowired private val userRepository: UserRepository) : IUserService {

    override fun getUserByAuth0Id(authO: String): User {
        var user = userRepository.findUserByAuth0Id(authO)
        if (user == null) {
            user = userRepository.save(User(authO))
        }
        return user
    }

    override fun removeCampaignFromUser(user: User, campaign: Campaign): User {
        if (campaign.owner.id == user.id) {
            user.campaignOwner.removeIf { it.id == campaign.id }
        } else {
            user.campaignMember.removeIf { it.campaign.id == campaign.id }
        }
        return userRepository.save(user)
    }
}