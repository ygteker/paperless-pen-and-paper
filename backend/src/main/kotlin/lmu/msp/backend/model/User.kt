package lmu.msp.backend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
class User(
    @Column(nullable = false, unique = true)
    @JsonIgnore //frontend doesn't need this value.
    val auth0Id: String,

    @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
    val campaignOwner: MutableList<Campaign> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val campaignMember: List<CampaignMember> = emptyList()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    val id: Long = 0

    override fun toString(): String {
        return "User(auth0Id='$auth0Id', campaignOwner=$campaignOwner, campaignMember=$campaignMember, id=$id)"
    }


}