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
        summary: String? = null
    ) {
        this.writing = Writing(title, summary)
        this.writing.setBy(topic)
    }

    fun addImage(order: Int, path: String, thumbnail: String? = null) {
        this.writing.addBy(
            Attachment(Attachment.Type.IMAGE, order, path).apply { this.thumbnail = thumbnail }
        )
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
