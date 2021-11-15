package io.mustelidae.smallclawedotter.api.domain.board.repository

import io.mustelidae.smallclawedotter.api.domain.board.QAttachment.attachment
import io.mustelidae.smallclawedotter.api.domain.board.QParagraph.paragraph
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
            .innerJoin(writing.topic, topic).fetchJoin()
            .where(
                writing.expired.isFalse
                    .and(topic.code.eq(code))
                    .and(writing.effectiveDate.loe(now).or(writing.effectiveDate.isNull))
                    .and(writing.expirationDate.goe(now).or(writing.expirationDate.isNull))
            ).fetch()
    }

    fun findOne(id: Long): Writing? {
        return from(writing)
            .innerJoin(writing.topic, topic).fetchJoin()
            .leftJoin(writing.paragraph, paragraph).fetchJoin()
            .leftJoin(writing.attachments, attachment).fetchJoin()
            .where(writing.id.eq(id))
            .fetchOne()
    }
}
