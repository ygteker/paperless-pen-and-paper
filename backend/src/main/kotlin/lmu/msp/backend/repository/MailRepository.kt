package lmu.msp.backend.repository

import lmu.msp.backend.model.Mail
import lmu.msp.backend.model.User
import org.springframework.data.geo.GeoResult
import org.springframework.data.jpa.repository.JpaRepository

interface MailRepository : JpaRepository<Mail, Long> {

    fun findBySender_IdOrReceiver_Id(id: Long, id1: Long): List<Mail>

}
