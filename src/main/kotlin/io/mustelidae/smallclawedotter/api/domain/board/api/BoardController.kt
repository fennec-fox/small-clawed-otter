package io.mustelidae.smallclawedotter.api.domain.board.api

import io.mustelidae.smallclawedotter.api.common.ProductCode
import io.mustelidae.smallclawedotter.api.common.Replies
import io.mustelidae.smallclawedotter.api.common.Reply
import io.mustelidae.smallclawedotter.api.common.toReplies
import io.mustelidae.smallclawedotter.api.common.toReply
import io.mustelidae.smallclawedotter.api.domain.board.WritingFinder
import io.mustelidae.smallclawedotter.api.domain.permission.TopicRelationChecker
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/product/{productCode}/topics/{topicCode}")
class BoardController(
    private val writingFinder: WritingFinder
) {

    @GetMapping("articles")
    fun findAll(
        @PathVariable productCode: ProductCode,
        @PathVariable topicCode: String,
    ): Replies<BoardResources.Reply.Article> {
        val writings = writingFinder.findAll(topicCode)

        TopicRelationChecker(productCode, topicCode).checkWritings(writings)

        return writings
            .filter { it.isHidden().not() }
            .map { BoardResources.Reply.Article.from(it) }
            .toReplies()
    }

    @GetMapping("articles/{id}")
    fun findOne(
        @PathVariable productCode: ProductCode,
        @PathVariable topicCode: String,
        @PathVariable id: Long
    ): Reply<BoardResources.Reply.ArticleWithParagraph> {
        val writing = writingFinder.findOne(id)

        TopicRelationChecker(productCode, topicCode).checkWriting(writing)

        return BoardResources.Reply.ArticleWithParagraph.from(writing)
            .toReply()
    }
}
