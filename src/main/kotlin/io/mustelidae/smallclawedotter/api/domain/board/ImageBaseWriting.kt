package io.mustelidae.smallclawedotter.api.domain.board

import io.mustelidae.smallclawedotter.api.domain.topic.Topic
import java.time.LocalDateTime

/**
 * only image writing
 */
class ImageBaseWriting {
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
        attachments: List<Attachment>,
        summary: String? = null
    ) {
        this.writing = Writing(title, summary)
        this.writing.setBy(topic)
        attachments.forEach { it.setBy(writing) }
    }

    fun setTerm(start: LocalDateTime, end: LocalDateTime) {
        this.writing.setTerm(start, end)
    }

    fun modify(
        title: String,
        summary: String? = null
    ) {
        this.writing.apply {
            this.title = title
            this.summary = summary
        }
    }
}
