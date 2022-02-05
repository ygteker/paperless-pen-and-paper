package lmu.msp.backend.repository

import lmu.msp.backend.model.Campaign
import org.springframework.data.jpa.repository.JpaRepository

/**
 * jpa repository
 * used to generate queries to the DB
 * campaigns doesn't need "special/custom" queries
 *
 */
interface CampaignRepository : JpaRepository<Campaign, Long> {

}