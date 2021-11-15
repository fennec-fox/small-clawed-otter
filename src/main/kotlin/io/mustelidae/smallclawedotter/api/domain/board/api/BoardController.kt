package io.mustelidae.smallclawedotter.api.domain.board.api

import io.mustelidae.smallclawedotter.api.common.ProductCode
import io.mustelidae.smallclawedotter.api.common.Replies
import io.mustelidae.smallclawedotter.api.common.Reply
import io.mustelidae.smallclawedotter.api.common.toReplies
import io.mustelidae.smallclawedotter.api.common.toReply
import io.mustelidae.smallclawedotter.api.domain.board.BoardInteraction
import io.mustelidae.smallclawedotter.api.domain.board.WritingFinder
import io.mustelidae.smallclawedotter.api.domain.permission.RoleHeader
import io.mustelidae.smallclawedotter.api.domain.permission.TopicRelationChecker
import io.mustelidae.smallclawedotter.api.domain.topic.TopicFinder
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/product/{productCode}/topics/{topicCode}")
class BoardController(
    private val boardInteraction: BoardInteraction,
    private val topicFinder: TopicFinder,
    private val writingFinder: WritingFinder
) {

    @PostMapping("text-article")
    @ResponseStatus(HttpStatus.CREATED)
    fun writeTextArticle(
        @RequestHeader(RoleHeader.XAdmin.KEY) adminId: Long,
        @PathVariable productCode: ProductCode,
        @PathVariable topicCode: String,
        @RequestBody request: BoardResources.Request.TextDoc
    ): Reply<Long> {

        val topic = topicFinder.findOne(topicCode)
        val writing = boardInteraction.write(topic, request)
        return writing.id!!.toReply()
    }

    @PostMapping("image-article")
    @ResponseStatus(HttpStatus.CREATED)
    fun writeImageArticle(
        @RequestHeader(RoleHeader.XAdmin.KEY) adminId: Long,
        @PathVariable productCode: ProductCode,
        @PathVariable topicCode: String,
        @RequestBody request: BoardResources.Request.ImageDoc
    ): Reply<Long> {
        val topic = topicFinder.findOne(topicCode)
        val writing = boardInteraction.write(topic, request)
        return writing.id!!.toReply()
    }

    @GetMapping("articles")
    fun findAll(
        @PathVariable productCode: ProductCode,
        @PathVariable topicCode: String,
    ): Replies<BoardResources.Reply.ArticleSummary> {
        val topic = topicFinder.findOne(topicCode)
        val writings = writingFinder.findAll(topic)

        TopicRelationChecker(productCode, topic).checkWritings(writings)

        return writings
            .filter { it.isHidden().not() }
            .map { BoardResources.Reply.ArticleSummary.from(it) }
            .toReplies()
    }

    @GetMapping("articles/{id}")
    fun findOne(
        @PathVariable productCode: ProductCode,
        @PathVariable topicCode: String,
        @PathVariable id: Long
    ): Reply<BoardResources.Reply.ArticleWithParagraph> {
        val topic = topicFinder.findOne(topicCode)
        val writing = writingFinder.findOne(id)

        TopicRelationChecker(productCode, topic).checkWriting(writing)

        return BoardResources.Reply.ArticleWithParagraph.from(writing)
            .toReply()
    }
}
