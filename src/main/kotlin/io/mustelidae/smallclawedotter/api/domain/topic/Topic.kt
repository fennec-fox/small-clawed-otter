package io.mustelidae.smallclawedotter.api.domain.topic

import io.mustelidae.smallclawedotter.api.common.Audit
import io.mustelidae.smallclawedotter.api.domain.board.Document
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
    var documents: MutableList<Document> = arrayListOf()
        protected set

    var expired = false

    fun expire() {
        this.expired = true
    }

    fun addBy(document: Document) {
        documents.add(document)
        if (document.topic != this)
            document.setBy(this)
    }
}
