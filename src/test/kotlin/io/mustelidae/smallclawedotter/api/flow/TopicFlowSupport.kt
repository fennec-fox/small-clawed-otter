package io.mustelidae.smallclawedotter.api.flow

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mustelidae.smallclawedotter.api.common.ProductCode
import io.mustelidae.smallclawedotter.api.common.Replies
import io.mustelidae.smallclawedotter.api.common.Reply
import io.mustelidae.smallclawedotter.api.domain.permission.RoleHeader
import io.mustelidae.smallclawedotter.api.domain.topic.api.TopicController
import io.mustelidae.smallclawedotter.api.domain.topic.api.TopicResources
import io.mustelidae.smallclawedotter.api.utils.fromJson
import io.mustelidae.smallclawedotter.api.utils.toJson
import io.mustelidae.smallclawedotter.utils.FixtureID
import io.mustelidae.smallclawedotter.utils.target
import org.bson.types.ObjectId
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

class TopicFlowSupport(
    private val mockMvc: MockMvc,
    val code: String = ObjectId().toString(),
    val request: TopicResources.Request = TopicResources.Request(code, "Test Topic", "Flow test"),
    private val adminId: Long = FixtureID.userId()
) {
    val productCode = ProductCode.NOTICE

    fun add(): Long {
        return mockMvc.post(linkTo<TopicController> { add(adminId, productCode, request) }.toUri()) {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = request.toJson()
            header(RoleHeader.XAdmin.KEY, adminId)
        }.andExpect {
            status { is2xxSuccessful() }
            content {
                target<Reply<Long>> {
                    it.content shouldNotBe -1
                }
            }
        }.andReturn()
            .response
            .contentAsString
            .fromJson<Reply<Long>>()
            .content!!
    }

    fun find(): TopicResources.Reply {
        return mockMvc.get(linkTo<TopicController> { findOne(productCode, code) }.toUri()) {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
            content {
                target<Reply<TopicResources.Reply>> {
                    val topic = it.content!!
                    topic.name shouldNotBe null
                    topic.description shouldNotBe null
                    topic.code shouldBe code
                }
            }
        }.andReturn()
            .response
            .contentAsString
            .fromJson<Reply<TopicResources.Reply>>()
            .content!!
    }

    fun findAll(): List<TopicResources.Reply> {
        return mockMvc.get(linkTo<TopicController> { findAll(productCode) }.toUri()) {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
        }.andReturn()
            .response
            .contentAsString
            .fromJson<Replies<TopicResources.Reply>>()
            .getContent()
            .toList()
    }

    fun modify(
        modify: TopicResources.Modify
    ) {
        mockMvc.put(linkTo<TopicController> { modify(productCode, code, modify) }.toUri()) {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = modify.toJson()
        }.andExpect {
            status { is2xxSuccessful() }
        }
    }

    fun remove() {
        mockMvc.delete(linkTo<TopicController> { remove(productCode, code) }.toUri()) {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
        }
    }
}
