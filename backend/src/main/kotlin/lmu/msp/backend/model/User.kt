package lmu.msp.backend.model

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import javax.persistence.*

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
class User(
    @Column(nullable = false, unique = true)
    @JsonIgnore //frontend doesn't need this value.
    val auth0Id: String,

    @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
    val campaignOwner: MutableList<Campaign> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
    val campaignMember: MutableList<CampaignMember> = mutableListOf(),

    @OneToMany(mappedBy = "sender", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    val sendMails: MutableList<Mail> = mutableListOf(),

    @OneToMany(mappedBy = "receiver", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    val receivedMails: MutableList<Mail> = mutableListOf()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    val id: Long = 0

}