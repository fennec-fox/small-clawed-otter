package io.mustelidae.smallclawedotter.api.domain.board

import io.mustelidae.smallclawedotter.api.domain.topic.Topic
import java.time.LocalDateTime

/**
 * Text base writing
 */
class TextBaseWriting {
    constructor(topic: Topic) {
        this.topic = topic
    }
    constructor(writing: Writing) {
        this.writing = writing
        this.topic = writing.topic!!
    }

    lateinit var writing: Writing
        private set
    private val topic: Topic

    fun write(
        title: String,
        type: Paragraph.Type,
        text: String,
        summary: String? = null
    ) {
        this.writing = Writing(title, summary)
        this.writing.setBy(topic)
        this.writing.setBy(
            Paragraph(type, text)
        )
    }

    fun modify(
        title: String,
        text: String,
        summary: String? = null,
    ) {
        this.writing.apply {
            this.title = title
            this.summary = summary
            this.paragraph!!.text = text
        }
    }

    fun setTerm(start: LocalDateTime, end: LocalDateTime) {
        this.writing.setTerm(start, end)
    }

    fun addAttachment(type: Attachment.Type, order: Int, path: String) {
        writing.addBy(
            Attachment(type, order, path)
        )
    }

    fun removeAttachment(id: Long) {
        val attachment = writing.attachments.find { it.id == id }
        attachment?.expire()
    }
}
