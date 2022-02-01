package lmu.msp.backend.model

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import javax.persistence.*

/**
 * User jpa def
 * from this the jpa will generate a storage (e.g. database, depends on the application.properties configuration)
 *
 *
 * @property auth0Id
 * @property campaignOwner
 * @property campaignMember
 * @property sendMails
 * @property receivedMails
 * @property image
 */
@Entity
class User(
    @Column(nullable = false, unique = true)
    @JsonIgnore //frontend doesn't need this value.
    val auth0Id: String,

    @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
    val campaignOwner: MutableList<Campaign> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
    val campaignMember: MutableList<CampaignMember> = mutableListOf(),

    @OneToMany(mappedBy = "sender", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    val sendMails: MutableList<Mail> = mutableListOf(),

    @OneToMany(mappedBy = "receiver", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    val receivedMails: MutableList<Mail> = mutableListOf(),

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false, length = 100000)
    @JsonIgnore
    var image: ByteArray = ByteArray(0)
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    val id: Long = 0

}