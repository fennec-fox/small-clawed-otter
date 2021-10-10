package io.mustelidae.smallclawedotter.domain.board

import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.OneToOne

/**
 * 공지사항용으로 사용할 문서
 * - paragraph에는 lob 형태의 column이 있어서 별도 테이블로 분리한다.
 * @ref https://dev.mysql.com/doc/refman/8.0/en/blob.html
 */
@Entity
class Document(
    @Column(length = 1000)
    val title: String,
    @Column(length = 2000)
    val summary: String? = null
) {

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "paragraph_id")
    var paragraph: Paragraph? = null

    @OneToMany(mappedBy = "document", cascade = [CascadeType.ALL])
    var attachments: MutableList<Attachment> = arrayListOf()

    @Id
    @GeneratedValue
    var id: Long? = null
        private set

    var expired = false
        private set

    var effectiveDate: LocalDateTime? = null
        private set
    var expirationDate: LocalDateTime? = null
        private set

    var hidden = false
        private set
    var showDateTime: LocalDateTime? = null
        private set

    fun setBy(paragraph: Paragraph) {
        this.paragraph = paragraph
        if(paragraph.document != this)
            paragraph.setBy(this)
    }

    fun addBy(attachment: Attachment) {
        attachments.add(attachment)
        if(attachment.document != this)
            attachment.setBy(this)
    }

    fun setTerm(start: LocalDateTime, end: LocalDateTime) {
        this.effectiveDate = start
        this.expirationDate = end
    }

    fun onHidden(restoreHiddenDateTime: LocalDateTime) {
        this.hidden = true
        this.showDateTime = restoreHiddenDateTime
    }

    fun offHidden() {
        this.hidden = false
        this.showDateTime = null
    }

    fun isHidden(): Boolean {
        if(hidden.not())
            return false

        return showDateTime!!.isAfter(LocalDateTime.now())
    }

    fun expire() {
        this.expired = true
    }


}