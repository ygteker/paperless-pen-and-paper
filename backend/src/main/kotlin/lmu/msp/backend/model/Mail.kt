package lmu.msp.backend.model

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import javax.persistence.*

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
class Mail(

    @ManyToOne(fetch = FetchType.EAGER)
    val sender: User,
    @ManyToOne(fetch = FetchType.EAGER)
    val receiver: User,
    @Column(nullable = false)
    val string: String

) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    val id: Long = 0
}