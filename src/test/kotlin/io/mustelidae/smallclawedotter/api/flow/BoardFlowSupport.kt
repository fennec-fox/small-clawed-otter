package io.mustelidae.smallclawedotter.api.flow

import io.mustelidae.smallclawedotter.api.common.ProductCode
import io.mustelidae.smallclawedotter.api.common.Replies
import io.mustelidae.smallclawedotter.api.common.Reply
import io.mustelidae.smallclawedotter.api.domain.board.Paragraph
import io.mustelidae.smallclawedotter.api.domain.board.api.BoardController
import io.mustelidae.smallclawedotter.api.domain.board.api.BoardMaintenanceController
import io.mustelidae.smallclawedotter.api.domain.board.api.BoardResources
import io.mustelidae.smallclawedotter.api.domain.permission.RoleHeader
import io.mustelidae.smallclawedotter.api.utils.fromJson
import io.mustelidae.smallclawedotter.api.utils.toJson
import io.mustelidae.smallclawedotter.utils.FixtureID
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.time.LocalDateTime

class BoardFlowSupport(
    private val mockMvc: MockMvc,
    private val productCode: ProductCode,
    private val topicCode: String,
    private val adminId: Long = FixtureID.userId(),
) {

    fun writeText(
        request: BoardResources.Request.TextDoc = BoardResources.Request.TextDoc(
            "Test Article",
            Paragraph.Type.MARKDOWN,
            "## This is a H2",
            "This is summary",
            LocalDateTime.now().minusDays(1),
            LocalDateTime.now().plusDays(1),
            listOf(
                BoardResources.Request.TextDoc.File(1, "https://www.naver.com/")
            )
        )
    ): Long {

        return mockMvc.post(linkTo<BoardMaintenanceController> { writeTextArticle(adminId, productCode, topicCode, request) }.toUri()) {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = request.toJson()
            header(RoleHeader.XAdmin.KEY, adminId)
        }.andExpect {
            status { isCreated() }
        }.andReturn()
            .response.contentAsString
            .fromJson<Reply<Long>>()
            .content!!
    }

    fun writeImage(
        request: BoardResources.Request.ImageDoc = BoardResources.Request.ImageDoc(
            "Test Image Article",
            listOf(
                BoardResources.Request.ImageDoc.Image(1, "https://ssl.gstatic.com/ui/v1/icons/mail/rfr/logo_gmail_lockup_default_2x_r2.png"),
                BoardResources.Request.ImageDoc.Image(2, "https://papago.naver.com/97ec80a681e94540414daf2fb855ba3b.svg")
            )
        )
    ): Long {
        return mockMvc.post(linkTo<BoardMaintenanceController> { writeImageArticle(adminId, productCode, topicCode, request) }.toUri()) {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = request.toJson()
            header(RoleHeader.XAdmin.KEY, adminId)
        }.andExpect {
            status { isCreated() }
        }.andReturn()
            .response.contentAsString
            .fromJson<Reply<Long>>()
            .content!!
    }

    fun findAll(): List<BoardResources.Reply.Article> {
        return mockMvc.get(linkTo<BoardController> { findAll(productCode, topicCode) }.toUri()) {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
        }.andReturn()
            .response.contentAsString
            .fromJson<Replies<BoardResources.Reply.Article>>()
            .getContent()
            .toList()
    }

    fun findOne(id: Long): BoardResources.Reply.ArticleWithParagraph {
        return mockMvc.get(linkTo<BoardController> { findOne(productCode, topicCode, id) }.toUri()) {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }.andExpect {
            status { is2xxSuccessful() }
        }.andReturn()
            .response.contentAsString
            .fromJson<Reply<BoardResources.Reply.ArticleWithParagraph>>()
            .content!!
    }

    fun modify(articleId: Long, modify: BoardResources.Modify.TextDoc) {
        mockMvc.put(linkTo<BoardMaintenanceController> { modifyTextArticle(adminId, productCode, topicCode, articleId, modify) }.toUri()) {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = modify.toJson()
            header(RoleHeader.XAdmin.KEY, adminId)
        }.andExpect {
            status { is2xxSuccessful() }
        }
    }

    fun modify(articleId: Long, modify: BoardResources.Modify.ImageDoc) {
        mockMvc.put(linkTo<BoardMaintenanceController> { modifyImageArticle(adminId, productCode, topicCode, articleId, modify) }.toUri()) {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = modify.toJson()
            header(RoleHeader.XAdmin.KEY, adminId)
        }.andExpect {
            status { is2xxSuccessful() }
        }
    }
}
