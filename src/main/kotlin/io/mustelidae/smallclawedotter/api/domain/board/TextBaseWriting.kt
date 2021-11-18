package io.mustelidae.smallclawedotter.api.domain.board

import io.mustelidae.smallclawedotter.api.config.PreconditionFailException
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
        if (writing.type != Writing.Type.TEXT)
            throw PreconditionFailException("해당 글은 text 타입이 아닙니다.")

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
        this.writing = Writing(Writing.Type.TEXT, title, summary)
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

    fun setTerm(startTerm: LocalDateTime, endTerm: LocalDateTime) {
        this.writing.setTerm(startTerm, endTerm)
    }

    fun addAttachment(type: Attachment.Type, order: Int, path: String) {
        writing.addBy(
            Attachment(type, order, path)
        )
    }
}
