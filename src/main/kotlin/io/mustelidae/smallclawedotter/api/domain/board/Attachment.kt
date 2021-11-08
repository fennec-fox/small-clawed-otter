package io.mustelidae.smallclawedotter.api.domain.board

import io.mustelidae.smallclawedotter.api.common.Audit
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
) : Audit() {

    @Id
    @GeneratedValue
    var id: Long? = null
        protected set

    @Column(length = 1500)
    var thumbnail: String? = null

    var expired = false

    enum class Type {
        FILE,
        IMAGE
    }

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    var writing: Writing? = null
        protected set

    fun setBy(writing: Writing) {
        this.writing = writing
        if (writing.attachments.contains(this).not())
            writing.addBy(this)
    }

    fun expire() {
        this.expired = true
    }
}
