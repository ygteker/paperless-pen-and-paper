package lmu.msp.backend.repository

import lmu.msp.backend.model.CampaignMember
import org.springframework.data.jpa.repository.JpaRepository

/**
 * jpa repository
 * used to generate queries to the DB
 * jpa will generate queries from the functions
 *
 */
interface MemberRepository : JpaRepository<CampaignMember, Long> {

    fun findByCampaignIdAndUserId(campaignId: Long, userId: Long): CampaignMember?

    fun findByCampaignIdAndUserAuth0Id(campaignId: Long, auth0Id: String): CampaignMember?

    fun deleteByCampaignId(campaignId: Long)

}