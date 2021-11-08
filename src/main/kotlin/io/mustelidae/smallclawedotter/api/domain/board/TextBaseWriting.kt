package io.mustelidae.smallclawedotter.api.domain.board

import io.mustelidae.smallclawedotter.api.domain.topic.Topic
import java.time.LocalDateTime

/**
 * Text base document
 */
class TextBaseWriting {
    constructor(topic: Topic) {
        this.topic = topic
    }
    constructor(article: Article) {
        this.article = article
        this.topic = article.topic!!
    }

    lateinit var article: Article
        private set
    private val topic: Topic

    fun write(
        title: String,
        type: Paragraph.Type,
        text: String,
        summary: String? = null
    ) {
        this.article = Article(title, summary)
        this.article.setBy(topic)
        this.article.setBy(
            Paragraph(type, text)
        )
    }

    fun modify(
        title: String,
        text: String,
        summary: String? = null,
    ) {
        this.article.apply {
            this.title = title
            this.summary = summary
            this.paragraph!!.text = text
        }
    }

    fun setTerm(start: LocalDateTime, end: LocalDateTime) {
        this.article.setTerm(start, end)
    }

    fun addAttachment(type: Attachment.Type, order: Int, path: String) {
        article.addBy(
            Attachment(type, order, path)
        )
    }

    fun removeAttachment(id: Long) {
        val attachment = article.attachments.find { it.id == id }
        attachment?.expire()
    }
}
