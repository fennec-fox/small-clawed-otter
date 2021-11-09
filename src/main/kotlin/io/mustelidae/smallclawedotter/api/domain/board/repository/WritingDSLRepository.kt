package io.mustelidae.smallclawedotter.api.domain.board.repository

import io.mustelidae.smallclawedotter.api.domain.board.QWriting.writing
import io.mustelidae.smallclawedotter.api.domain.board.Writing
import io.mustelidae.smallclawedotter.api.domain.topic.QTopic.topic
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class WritingDSLRepository : QuerydslRepositorySupport(Writing::class.java) {

    fun findAllByTopic(code: String): List<Writing>? {
        val now = LocalDateTime.now()

        return from(writing)
            .innerJoin(writing.topic, topic)
            .where(
                writing.expired.isFalse
                    .and(topic.code.eq(code))
                    .and(writing.effectiveDate.goe(now))
                    .and(writing.expirationDate.gt(now))
            ).fetch()
    }
}
