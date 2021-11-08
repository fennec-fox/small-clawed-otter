package io.mustelidae.smallclawedotter.api.domain.board

import io.mustelidae.smallclawedotter.api.domain.topic.Topic
import java.time.LocalDateTime

/**
 * only image document
 */
class ImageBaseWriting {
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
        attachments: List<Attachment>,
        summary: String? = null
    ) {
        this.document = Document(title, summary)
        this.document.setBy(topic)
        attachments.forEach { it.setBy(document) }
    }

    fun setTerm(start: LocalDateTime, end: LocalDateTime) {
        this.document.setTerm(start, end)
    }

    fun modify(
        title: String,
        summary: String? = null
    ) {
        this.document.apply {
            this.title = title
            this.summary = summary
        }
    }
}
