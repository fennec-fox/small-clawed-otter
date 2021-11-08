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
    constructor(article: Article) {
        this.article = article
        this.topic = article.topic!!
    }

    lateinit var article: Article
        private set
    private val topic: Topic

    fun write(
        title: String,
        attachments: List<Attachment>,
        summary: String? = null
    ) {
        this.article = Article(title, summary)
        this.article.setBy(topic)
        attachments.forEach { it.setBy(article) }
    }

    fun setTerm(start: LocalDateTime, end: LocalDateTime) {
        this.article.setTerm(start, end)
    }

    fun modify(
        title: String,
        summary: String? = null
    ) {
        this.article.apply {
            this.title = title
            this.summary = summary
        }
    }
}
