package io.mustelidae.smallclawedotter.api.domain.board.api

import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
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
            BoardResources.Request.TextArticle(
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
            BoardResources.Request.ImageArticle(
                "already past time",
                listOf(BoardResources.Request.ImageArticle.Image(1, "")),
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
        val request = BoardResources.Request.TextArticle(
            "Test Article Has Detail",
            Paragraph.Type.MARKDOWN,
            "## This is a H2",
            "This is summary",
            files = listOf(
                BoardResources.Request.TextArticle.File(1, "https://www.naver.com/")
            )
        )
        // When
        val id = boardFlowSupport.writeText(request)
        // Then
        val article = boardFlowSupport.findOne(id)
        article.asClue {
            it.title shouldBe request.title
            it.paragraph!!.text shouldBe request.text
            it.paragraph
        }
    }

    @Test
    @Order(4)
    fun modifyText() {
        // Given
        val request = BoardResources.Request.TextArticle(
            "Original Title",
            Paragraph.Type.MARKDOWN,
            "## This is a Original Text H2",
            "This is summary",
            files = listOf(
                BoardResources.Request.TextArticle.File(1, "https://www.naver.com/")
            )
        )

        val modify = BoardResources.Modify.TextArticle(
            "Modified Title",
            "### This is a Modified Text H3",
            null,
            startTerm = LocalDateTime.now().plusDays(1),
            endTerm = LocalDateTime.now().plusDays(4)
        )

        val boardFlowSupport = BoardFlowSupport(mockMvc, productCode, topic.code)
        val id = boardFlowSupport.writeText()
        // When
        boardFlowSupport.modify(id, modify)
        // Then
        val article = boardFlowSupport.findOne(id)

        article.asClue {
            it.title shouldBe modify.title
            it.paragraph!!.text shouldBe modify.text
            it.startTerm!! shouldNotBe null
            it.endTerm!! shouldNotBe null
            it.attachments!!.size shouldBe request.files!!.size
        }
    }

    @Test
    @Order(5)
    fun modifyImage() {
        // Given
        val request = BoardResources.Request.ImageArticle(
            "Original Image Title",
            listOf(
                BoardResources.Request.ImageArticle.Image(
                    1,
                    "http://1.com"
                ),
                BoardResources.Request.ImageArticle.Image(
                    2,
                    "http://2.com"
                )
            )
        )

        val modify = BoardResources.Modify.ImageArticle(
            "Modify Image Title",
            listOf(
                BoardResources.Modify.ImageArticle.Image(
                    1,
                    "http://modify.com"
                ),
                BoardResources.Modify.ImageArticle.Image(
                    2,
                    "http://modify.com"
                )
            )
        )

        val boardFlowSupport = BoardFlowSupport(mockMvc, productCode, topic.code)
        val id = boardFlowSupport.writeImage(request)

        // When
        boardFlowSupport.modify(id, modify)

        // Then
        val article = boardFlowSupport.findOne(id)

        article.asClue {
            it.attachments!!.size shouldBe 2
            it.title shouldBe modify.title
            it.paragraph shouldBe null
            it.attachments!!.forEach { attach ->
                attach.path shouldBe modify.images.first().path
            }
        }
    }
}
