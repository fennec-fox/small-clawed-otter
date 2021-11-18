package io.mustelidae.smallclawedotter.api.domain.topic

import io.mustelidae.smallclawedotter.api.common.Audit
import io.mustelidae.smallclawedotter.api.common.ProductCode
import io.mustelidae.smallclawedotter.api.domain.board.Writing
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class Topic(
    @Column(length = 100)
    @Enumerated(EnumType.STRING)
    val productCode: ProductCode,
    @Column(length = 45, unique = true)
    val code: String,
    @Column(length = 200)
    var name: String,
    @Column(length = 500)
    var description: String? = null,
) : Audit() {

    @Id
    @GeneratedValue
    var id: Long? = null
        protected set

    @OneToMany(mappedBy = "topic")
    var writings: MutableList<Writing> = arrayListOf()
        protected set

    var expired = false

    fun expire() {
        this.expired = true
    }

    fun addBy(writing: Writing) {
        writings.add(writing)
        if (writing.topic != this)
            writing.setBy(this)
    }

    companion object
}
