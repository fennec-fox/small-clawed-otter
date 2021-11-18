package io.mustelidae.smallclawedotter.api.domain.board.api

import io.mustelidae.smallclawedotter.api.common.ProductCode
import io.mustelidae.smallclawedotter.api.common.Reply
import io.mustelidae.smallclawedotter.api.common.toReply
import io.mustelidae.smallclawedotter.api.domain.board.BoardInteraction
import io.mustelidae.smallclawedotter.api.domain.permission.RoleHeader
import io.mustelidae.smallclawedotter.api.domain.permission.TopicRelationChecker
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/maintenance/product/{productCode}/topics/{topicCode}")
class BoardMaintenanceController(
    private val boardInteraction: BoardInteraction
) {

    @PostMapping("text-article")
    @ResponseStatus(HttpStatus.CREATED)
    fun writeTextArticle(
        @RequestHeader(RoleHeader.XAdmin.KEY) adminId: Long,
        @PathVariable productCode: ProductCode,
        @PathVariable topicCode: String,
        @RequestBody request: BoardResources.Request.TextArticle
    ): Reply<Long> {
        val writing = boardInteraction.write(topicCode, request)
        TopicRelationChecker(productCode, topicCode).checkWriting(writing)
        return writing.id!!.toReply()
    }

    @PutMapping("text-article/{articleId}")
    fun modifyTextArticle(
        @RequestHeader(RoleHeader.XAdmin.KEY) adminId: Long,
        @PathVariable productCode: ProductCode,
        @PathVariable topicCode: String,
        @PathVariable articleId: Long,
        @RequestBody modify: BoardResources.Modify.TextArticle
    ): Reply<Unit> {
        val writing = boardInteraction.modify(articleId, modify)
        TopicRelationChecker(productCode, topicCode).checkWriting(writing)
        return Unit.toReply()
    }

    @PostMapping("image-article")
    @ResponseStatus(HttpStatus.CREATED)
    fun writeImageArticle(
        @RequestHeader(RoleHeader.XAdmin.KEY) adminId: Long,
        @PathVariable productCode: ProductCode,
        @PathVariable topicCode: String,
        @RequestBody request: BoardResources.Request.ImageArticle
    ): Reply<Long> {
        val writing = boardInteraction.write(topicCode, request)
        TopicRelationChecker(productCode, topicCode).checkWriting(writing)
        return writing.id!!.toReply()
    }

    @PutMapping("image-article/{articleId}")
    fun modifyImageArticle(
        @RequestHeader(RoleHeader.XAdmin.KEY) adminId: Long,
        @PathVariable productCode: ProductCode,
        @PathVariable topicCode: String,
        @PathVariable articleId: Long,
        @RequestBody modify: BoardResources.Modify.ImageArticle
    ): Reply<Unit> {
        val writing = boardInteraction.modify(articleId, modify)
        TopicRelationChecker(productCode, topicCode).checkWriting(writing)
        return Unit.toReply()
    }
}
