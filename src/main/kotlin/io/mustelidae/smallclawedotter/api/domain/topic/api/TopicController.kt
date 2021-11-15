package io.mustelidae.smallclawedotter.api.domain.topic.api

import io.mustelidae.smallclawedotter.api.common.ProductCode
import io.mustelidae.smallclawedotter.api.common.Replies
import io.mustelidae.smallclawedotter.api.common.Reply
import io.mustelidae.smallclawedotter.api.common.toReplies
import io.mustelidae.smallclawedotter.api.common.toReply
import io.mustelidae.smallclawedotter.api.domain.permission.RoleHeader
import io.mustelidae.smallclawedotter.api.domain.topic.TopicFinder
import io.mustelidae.smallclawedotter.api.domain.topic.TopicInteraction
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/maintenance/product/{productCode}/topics")
class TopicController(
    private val topicInteraction: TopicInteraction,
    private val topicFinder: TopicFinder
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun add(
        @RequestHeader(RoleHeader.XAdmin.KEY) adminId: Long,
        @PathVariable productCode: ProductCode,
        @RequestBody request: TopicResources.Request
    ): Reply<Long> {
        return topicInteraction.add(
            productCode,
            ObjectId(request.code),
            request.name,
            request.description
        ).id!!.toReply()
    }

    @PutMapping("{code}")
    fun modify(
        @PathVariable productCode: ProductCode,
        @PathVariable code: String,
        @RequestBody modify: TopicResources.Modify
    ): Reply<Unit> {
        topicInteraction.modify(code, modify.name, modify.description)
        return Unit.toReply()
    }

    @GetMapping
    fun findAll(
        @PathVariable productCode: ProductCode
    ): Replies<TopicResources.Response> {
        return topicFinder.findAll(productCode)
            .map { TopicResources.Response.from(it) }
            .toReplies()
    }

    @GetMapping("{code}")
    fun findOne(
        @PathVariable productCode: ProductCode,
        @PathVariable code: String
    ): Reply<TopicResources.Response> {
        val topic = topicFinder.findOne(code)
        return TopicResources.Response.from(topic)
            .toReply()
    }

    @DeleteMapping("{code}")
    fun remove(
        @PathVariable productCode: ProductCode,
        @PathVariable code: String
    ): Reply<Unit> {
        topicInteraction.expire(code)
        return Unit.toReply()
    }
}
