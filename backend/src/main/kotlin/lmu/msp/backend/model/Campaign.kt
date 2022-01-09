package lmu.msp.backend.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import javax.persistence.*

@Entity
final class Campaign(
    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    val owner: User,
    @Column(nullable = false, length = 45)
    val title: String,
    @OneToMany(mappedBy = "campaign", cascade = [CascadeType.ALL],fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    val campaignMembers: MutableList<CampaignMember> = mutableListOf()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    val id: Long = 0

    init {
        owner.campaignOwner.add(this)
    }

}