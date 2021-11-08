package io.mustelidae.smallclawedotter.api.domain.topic

import io.mustelidae.smallclawedotter.api.common.Audit
import io.mustelidae.smallclawedotter.api.domain.board.Writing
import org.bson.types.ObjectId
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class Topic(
    @Column(length = 200)
    val description: String,
    @Column(length = 40)
    val code: String = ObjectId().toString()
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
}
