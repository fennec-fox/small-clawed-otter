package io.mustelidae.smallclawedotter.domain.board


import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
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
    val text: String
) {

    @Id
    @GeneratedValue
    var id: Long? = null
        private set
    @OneToOne(mappedBy = "paragraph")
    var document: Document? = null

    enum class Type {
        JSON,
        HTML,
        MARKDOWN
    }

    fun setBy(document: Document) {
        this.document = document
        if(document.paragraph != this)
            document.setBy(this)
    }
}