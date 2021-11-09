package io.mustelidae.smallclawedotter.api.domain.topic.api

import io.kotest.assertions.asClue
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.mustelidae.smallclawedotter.api.flow.TopicFlowSupport
import io.mustelidae.smallclawedotter.utils.IntegrationSupport
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class TopicControllerFlowTest : IntegrationSupport() {

    @Order(1)
    @Test
    fun modify() {
        // Given
        val modify = TopicResources.Modify(
            "Modified Text",
            "Modified Desc"
        )
        val topicFlowSupport = TopicFlowSupport(mockMvc)

        // When
        topicFlowSupport.add()
        topicFlowSupport.modify(modify)

        // Then
        val topic = topicFlowSupport.find()

        topic.asClue {
            it.name shouldBe modify.name
            it.description shouldBe modify.description
        }
    }

    @Order(2)
    @Test
    fun findAll() {
        // Given
        val topicFlowSupport = TopicFlowSupport(mockMvc)

        // When
        topicFlowSupport.add()

        // Then
        topicFlowSupport.findAll().size shouldBeGreaterThan 0
    }

    @Order(3)
    @Test
    fun remove() {
        // Given
        val topicFlowSupport = TopicFlowSupport(mockMvc)
        // When
        topicFlowSupport.add()
        topicFlowSupport.remove()

        // Then
        mockMvc.get(linkTo<TopicController> { findOne(topicFlowSupport.productCode, topicFlowSupport.code) }.toUri()) {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is4xxClientError() }
        }
    }
}
