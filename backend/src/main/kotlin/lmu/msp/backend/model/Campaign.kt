package lmu.msp.backend.model

import javax.persistence.*

@Entity
class Campaign(
    @ManyToOne(fetch = FetchType.LAZY)
    val owner: User,
    @Column(nullable = false, length = 45)
    val title: String,
    @OneToMany(mappedBy = "campaign", cascade = [CascadeType.ALL], orphanRemoval = true)
    val campaignMembers: List<CampaignMember> = emptyList()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    val id: Long = 0
}