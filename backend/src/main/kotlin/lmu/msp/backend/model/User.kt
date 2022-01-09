package lmu.msp.backend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import javax.persistence.*

@Entity
class User(
    @Column(nullable = false, unique = true)
    @JsonIgnore //frontend doesn't need this value.
    val auth0Id: String,

    @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    val campaignOwner: MutableList<Campaign> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    val campaignMember: MutableList<CampaignMember> = mutableListOf()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    val id: Long = 0

}