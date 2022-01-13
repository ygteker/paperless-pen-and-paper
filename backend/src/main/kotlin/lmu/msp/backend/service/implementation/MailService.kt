package lmu.msp.backend.service.implementation

import lmu.msp.backend.model.Mail
import lmu.msp.backend.repository.MailRepository
import lmu.msp.backend.service.IMailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MailService(
    @Autowired private val userService: UserService,
    @Autowired private val mailRepository: MailRepository
) : IMailService {

    override fun getMails(authO: String, contactId: Long?): List<Mail> {
        val sender = userService.getUserByAuth0Id(authO)
        return if (null == contactId) {
            mailRepository.findBySender_IdOrReceiver_Id(sender.id)
        } else {
            mailRepository.findBySender_IdAndReceiver_IdOrSender_IdAndReceiver_Id(sender.id, contactId)
        }
    }

    override fun sendMail(authO: String, mail: Mail): Mail? {
        TODO("Not yet implemented")
    }

    override fun delMail(authO: String, mailId: Long): Boolean {
        TODO("Not yet implemented")
    }
}