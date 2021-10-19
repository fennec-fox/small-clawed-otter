package io.mustelidae.smallclawedotter.api.domain.board

import io.mustelidae.smallclawedotter.api.domain.topic.Topic
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
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
        protected set

    @OneToMany(mappedBy = "document", cascade = [CascadeType.ALL])
    var attachments: MutableList<Attachment> = arrayListOf()
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    var topic: Topic? = null
        protected set

    @Id
    @GeneratedValue
    var id: Long? = null
        protected set

    var expired = false
        protected set

    var effectiveDate: LocalDateTime? = null
        protected set
    var expirationDate: LocalDateTime? = null
        protected set

    var hidden = false
        protected set
    var showDateTime: LocalDateTime? = null
        protected set

    fun setBy(paragraph: Paragraph) {
        this.paragraph = paragraph
        if (paragraph.document != this)
            paragraph.setBy(this)
    }

    fun addBy(attachment: Attachment) {
        attachments.add(attachment)
        if (attachment.document != this)
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
        if (hidden.not())
            return false

        return showDateTime!!.isAfter(LocalDateTime.now())
    }

    fun expire() {
        this.expired = true
    }

    fun setBy(topic: Topic) {
        this.topic = topic
        if (topic.documents.contains(this).not())
            topic.addBy(this)
    }

    companion object
}