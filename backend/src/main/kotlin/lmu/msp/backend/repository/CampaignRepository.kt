package lmu.msp.backend.repository

import lmu.msp.backend.model.Campaign
import org.springframework.data.jpa.repository.JpaRepository

interface CampaignRepository : JpaRepository<Campaign, Long> {

}