package lmu.msp.backend.model

import javax.persistence.*

@Entity
class ExampleUser(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    val id: Long = 0,
    @Column(name = "auth_0_id", nullable = false, unique = true)
    var auth0Id: String
) {



}