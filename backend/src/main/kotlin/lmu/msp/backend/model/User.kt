package lmu.msp.backend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
class User(
    @Column(nullable = false, unique = true)
    @JsonIgnore //frontend doesn't need this value.
    val auth0Id: String,

    @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL], orphanRemoval = true)
    val campaignOwner: List<Campaign> = emptyList(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val campaignMember: List<CampaignMember> = emptyList()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    val id: Long = 0

}