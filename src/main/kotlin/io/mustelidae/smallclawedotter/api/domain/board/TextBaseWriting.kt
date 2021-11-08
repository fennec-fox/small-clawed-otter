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
    constructor(document: Document) {
        this.document = document
        this.topic = document.topic!!
    }

    lateinit var document: Document
        private set
    private val topic: Topic

    fun write(
        title: String,
        type: Paragraph.Type,
        text: String,
        summary: String? = null
    ) {
        this.document = Document(title, summary)
        this.document.setBy(topic)
        this.document.setBy(
            Paragraph(type, text)
        )
    }

    fun modify(
        title: String,
        text: String,
        summary: String? = null,
    ) {
        this.document.apply {
            this.title = title
            this.summary = summary
            this.paragraph!!.text = text
        }
    }

    fun setTerm(start: LocalDateTime, end: LocalDateTime) {
        this.document.setTerm(start, end)
    }

    fun addAttachment(type: Attachment.Type, order: Int, path: String) {
        document.addBy(
            Attachment(type, order, path)
        )
    }

    fun removeAttachment(id: Long) {
        val attachment = document.attachments.find { it.id == id }
        attachment?.expire()
    }
}
