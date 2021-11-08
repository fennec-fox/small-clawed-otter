package io.mustelidae.smallclawedotter.api.domain.board.repository

import io.mustelidae.smallclawedotter.api.domain.board.Article
import io.mustelidae.smallclawedotter.api.domain.board.QDocument.document
import io.mustelidae.smallclawedotter.api.domain.topic.QTopic.topic
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ArticleDSLRepository : QuerydslRepositorySupport(Article::class.java) {

    fun findAllByTopic(code: String): List<Article>? {
        val now = LocalDateTime.now()

        return from(document)
            .innerJoin(document.topic, topic)
            .where(
                document.expired.isFalse
                    .and(topic.code.eq(code))
                    .and(document.effectiveDate.goe(now))
                    .and(document.expirationDate.gt(now))
            ).fetch()
    }
}
