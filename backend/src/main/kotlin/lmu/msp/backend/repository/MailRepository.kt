package lmu.msp.backend.repository

import lmu.msp.backend.model.Mail
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

/**
 * jpa repository
 * used to generate queries to the DB
 * jpa will generate queries from the functions
 *
 */
interface MailRepository : JpaRepository<Mail, Long> {

    fun findBySender_IdOrReceiver_Id(sender: Long, receiver: Long = sender): List<Mail>


    fun findBySender_IdAndReceiver_IdOrSender_IdAndReceiver_Id(
        sender: Long,
        receiver: Long,
        sender2: Long = receiver,
        receiver2: Long = sender
    ): List<Mail>

    @Transactional
    fun deleteBySender_IdAndIdAllIgnoreCase(senderId: Long, mailId: Long): Long

}
