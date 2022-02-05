package lmu.msp.backend.service.implementation

import lmu.msp.backend.model.Mail
import lmu.msp.backend.repository.MailRepository
import lmu.msp.backend.service.IMailService
import lmu.msp.backend.service.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * implementation of IMailService
 * contains the "business logic"
 *
 * @property userService
 * @property mailRepository
 */
@Service
class MailService(
    @Autowired private val userService: IUserService,
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

    override fun sendMail(authO: String, receiverId: Long, message: String): Mail? {
        val sender = userService.getUserByAuth0Id(authO)
        val receiver = userService.getUserById(receiverId) ?: return null

        val mail = Mail(sender, receiver, message)
        return mailRepository.save(mail)
    }

    override fun delMail(authO: String, mailId: Long): Boolean {
        val sender = userService.getUserByAuth0Id(authO)

        val delCount = mailRepository.deleteBySender_IdAndIdAllIgnoreCase(sender.id, mailId)
        return delCount == 1L
    }
}