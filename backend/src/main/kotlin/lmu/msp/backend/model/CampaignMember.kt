package lmu.msp.backend.model

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import javax.persistence.*


@Entity
class CampaignMember(

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    val campaign: Campaign,

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    val user: User,

    @Column(nullable = false, length = 45)
    var characterName: String

) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    val id: Long = 0

}