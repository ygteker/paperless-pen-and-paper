package lmu.msp.backend.model

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import org.hibernate.annotations.CreationTimestamp
import java.sql.Timestamp
import javax.persistence.*

/**
 * Mail jpa def
 * from this the jpa will generate a storage (e.g. database, depends on the application.properties configuration)
 *
 * @property sender
 * @property receiver
 * @property message
 */
@Entity
class Mail(

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    val sender: User,
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    val receiver: User,
    @Column(nullable = false)
    val message: String

) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    val id: Long = 0

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    val time: Timestamp = Timestamp(0)
}