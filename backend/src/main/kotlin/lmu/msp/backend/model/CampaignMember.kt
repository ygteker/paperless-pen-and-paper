package lmu.msp.backend.model

import com.fasterxml.jackson.annotation.JsonBackReference
import lmu.msp.backend.model.embeddableIds.CampaignMemberId
import javax.persistence.*

@Entity
class CampaignMember(
    @EmbeddedId
    private val campaignMemberId: CampaignMemberId,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("campaignId")
    @JsonBackReference
    val campaign: Campaign,
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JsonBackReference
    val user: User,
    @Column(nullable = false, length = 45)
    val characterName: String

) {

}