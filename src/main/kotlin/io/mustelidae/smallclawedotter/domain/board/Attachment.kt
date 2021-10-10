package io.mustelidae.smallclawedotter.domain.board

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

/**
 * 첨부파일
 */
@Entity
class Attachment(
    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    val type: Type,
    @Column(name = "att_order")
    val order: Int,
    @Column(length = 1500)
    var path: String
) {

    @Id
    @GeneratedValue
    var id: Long? = null
        private set

    @Column(length = 1500)
    var thumbnail: String? = null

    var expired = false

    enum class Type {
        FILE,
        IMAGE
    }

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    var document: Document? = null
        private set

    fun setBy(document: Document) {
        this.document = document
        if(document.attachments.contains(this).not())
            document.addBy(this)
    }

    fun expire() {
        this.expired = true
    }
}