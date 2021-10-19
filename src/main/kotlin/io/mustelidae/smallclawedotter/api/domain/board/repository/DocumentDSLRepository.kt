package io.mustelidae.smallclawedotter.api.domain.board.repository

import io.mustelidae.smallclawedotter.api.domain.board.Document
import io.mustelidae.smallclawedotter.api.domain.board.QDocument.document
import io.mustelidae.smallclawedotter.api.domain.topic.QTopic.topic
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class DocumentDSLRepository : QuerydslRepositorySupport(Document::class.java) {

    fun findAllByTopic(code: String): List<Document>? {
        return from(document)
            .innerJoin(document.topic, topic)
            .where(
                document.expired.isFalse
                    .and(topic.code.eq(code))
            ).fetch()
    }
}
