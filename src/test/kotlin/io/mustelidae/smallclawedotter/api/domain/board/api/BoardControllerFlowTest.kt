package io.mustelidae.smallclawedotter.api.domain.board.api

import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import io.mustelidae.smallclawedotter.api.common.ProductCode
import io.mustelidae.smallclawedotter.api.domain.board.Paragraph
import io.mustelidae.smallclawedotter.api.domain.topic.api.TopicResources
import io.mustelidae.smallclawedotter.api.flow.BoardFlowSupport
import io.mustelidae.smallclawedotter.api.flow.TopicFlowSupport
import io.mustelidae.smallclawedotter.utils.IntegrationSupport
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import java.time.LocalDateTime

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BoardControllerFlowTest : IntegrationSupport() {

    private lateinit var topic: TopicResources.Response
    private val productCode = ProductCode.NOTICE

    @BeforeEach
    fun beforeEach() {
        val topicCode = ObjectId().toString()
        val topicFlowSupport = TopicFlowSupport(mockMvc, topicCode)
        topicFlowSupport.add()
        topic = topicFlowSupport.find()
    }

    @Test
    @Order(1)
    fun addImageTest() {
        // Given
        val boardFlowSupport = BoardFlowSupport(mockMvc, productCode, topic.code)

        // Then
        boardFlowSupport.writeImage()
    }

    @Test
    @Order(2)
    fun addTextTest() {
        // Given
        val boardFlowSupport = BoardFlowSupport(mockMvc, productCode, topic.code)

        // Then
        boardFlowSupport.writeText()
    }

    @Test
    @Order(2)
    fun findAll() {
        // Given
        val boardFlowSupport = BoardFlowSupport(mockMvc, productCode, topic.code)

        // When
        boardFlowSupport.writeText()
        boardFlowSupport.writeText()
        boardFlowSupport.writeText()
        boardFlowSupport.writeText(
            BoardResources.Request.TextDoc(
                "Tobe",
                Paragraph.Type.MARKDOWN,
                "",
                startTerm = LocalDateTime.now().plusDays(1),
                endTerm = LocalDateTime.now().plusDays(4)
            )
        )
        boardFlowSupport.writeImage()
        boardFlowSupport.writeImage()
        boardFlowSupport.writeImage(
            BoardResources.Request.ImageDoc(
                "already past time",
                listOf(BoardResources.Request.ImageDoc.Image(1, "")),
                startTerm = LocalDateTime.now().minusDays(3),
                endTerm = LocalDateTime.now().minusDays(1)
            )
        )
        // Then

        val articleSummaries = boardFlowSupport.findAll()

        articleSummaries.size shouldBe 5
    }

    @Test
    @Order(3)
    fun findOne() {
        // Given
        val boardFlowSupport = BoardFlowSupport(mockMvc, productCode, topic.code)
        val request = BoardResources.Request.TextDoc(
            "Test Article Has Detail",
            Paragraph.Type.MARKDOWN,
            "## This is a H2",
            "This is summary",
            files = listOf(
                BoardResources.Request.TextDoc.File(1, "https://www.naver.com/")
            )
        )
        // When
        val id = boardFlowSupport.writeText(request)
        // Then
        val article = boardFlowSupport.findOne(id)
        article.asClue {
            it.title shouldBe request.title
            it.articleParagraph!!.text shouldBe request.text
            it.articleParagraph
        }
    }
}
