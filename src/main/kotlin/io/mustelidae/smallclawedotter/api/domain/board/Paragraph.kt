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
    var article: Article? = null
        protected set

    enum class Type {
        JSON,
        HTML,
        MARKDOWN
    }

    fun setBy(article: Article) {
        this.article = article
        if (article.paragraph != this)
            article.setBy(this)
    }
}
