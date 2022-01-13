package lmu.msp.backend.service

import lmu.msp.backend.model.Mail

interface IMailService {

    /**
     * select all mails for an user and return it. If there is an contactId (not null) it will only select mails from that user
     *
     * @param authO
     * @param contactId
     * @return
     */
    fun getMails(authO: String, contactId: Long?): List<Mail>

    /**
     * send an mail. The auth0 id == sender
     * if successful returns mail obj otherwise return null
     *
     * @param authO
     * @param mail
     * @return
     */
    fun sendMail(authO: String, mail: Mail): Mail?

    /**
     * delete the mail with mailId. The auth0 id must be the sender.
     * returns true if successful returns false if not
     *
     * @param authO
     * @param mailId
     * @return
     */
    fun delMail(authO: String, mailId: Long): Boolean
}