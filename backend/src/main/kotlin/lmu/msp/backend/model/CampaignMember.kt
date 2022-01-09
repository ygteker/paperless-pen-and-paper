package lmu.msp.backend.model

import com.fasterxml.jackson.annotation.JsonBackReference
import javax.persistence.*


@Entity
class CampaignMember(

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JsonBackReference
    val campaign: Campaign,

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JsonBackReference
    val user: User,

    @Column(nullable = false, length = 45)
    var characterName: String

) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    val id: Long = 0

}