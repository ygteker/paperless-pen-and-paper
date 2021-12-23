package lmu.msp.backend.model

import javax.persistence.*

@Entity
final class Campaign(
    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    val owner: User,
    @Column(nullable = false, length = 45)
    val title: String,
    @OneToMany(mappedBy = "campaign", cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
    val campaignMembers: List<CampaignMember> = emptyList()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    val id: Long = 0

    init {
        owner.campaignOwner.add(this)
    }

    override fun toString(): String {
        return "Campaign(owner=${owner.id}, title='$title', campaignMembers=$campaignMembers, id=$id)"
    }


}