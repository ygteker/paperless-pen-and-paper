package lmu.msp.backend.model.embeddableIds

import java.io.Serializable
import java.util.*
import javax.persistence.Embeddable

@Embeddable
data class CampaignMemberId(
    private val campaignId: Long,
    private val userId: Long
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CampaignMemberId

        if (campaignId != other.campaignId) return false
        if (userId != other.userId) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(campaignId, userId)
    }
}