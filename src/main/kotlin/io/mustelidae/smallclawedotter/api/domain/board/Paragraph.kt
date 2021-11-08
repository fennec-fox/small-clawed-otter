package io.mustelidae.smallclawedotter.api.domain.board

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.OneToOne

/**
 * 문단
 */
@Entity
class Paragraph(
    val type: Type,
    @Lob
    @Column(columnDefinition = "CLOB")
    var text: String
) {

    @Id
    @GeneratedValue
    var id: Long? = null
        protected set
    @OneToOne(mappedBy = "paragraph")
    var document: Document? = null
        protected set

    enum class Type {
        JSON,
        HTML,
        MARKDOWN
    }

    fun setBy(document: Document) {
        this.document = document
        if (document.paragraph != this)
            document.setBy(this)
    }
}
