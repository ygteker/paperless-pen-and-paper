package lmu.msp.backend.model

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import javax.persistence.*

/**
 * campaign jpa def
 * from this the jpa will generate a storage (e.g. database, depends on the application.properties configuration)
 *
 * @property owner
 * @property title
 * @property campaignMember
 */
@Entity
final class Campaign(
    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    val owner: User,

    @Column(nullable = false, length = 45)
    val title: String,

    @OneToMany(mappedBy = "campaign", cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    val campaignMember: MutableList<CampaignMember> = mutableListOf()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    val id: Long = 0

    init {
        owner.campaignOwner.add(this)
    }

}