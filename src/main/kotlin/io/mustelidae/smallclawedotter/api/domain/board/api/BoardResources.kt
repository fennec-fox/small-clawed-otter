package io.mustelidae.smallclawedotter.api.domain.board.api

import com.fasterxml.jackson.annotation.JsonRawValue
import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.mustelidae.smallclawedotter.api.domain.board.Attachment
import io.mustelidae.smallclawedotter.api.domain.board.Paragraph
import io.mustelidae.smallclawedotter.api.domain.board.Writing
import io.mustelidae.smallclawedotter.api.utils.KeepAsJsonDeserializer
import org.hibernate.validator.constraints.URL
import java.time.LocalDateTime

class BoardResources {

    class Request {
        data class TextDoc(
            val title: String,
            val type: Paragraph.Type,
            val text: String,
            val summary: String? = null,
            val startTerm: LocalDateTime? = null,
            val endTerm: LocalDateTime? = null,
            val files: List<File>? = null
        ) {
            data class File(
                val order: Int,
                @get:URL
                val path: String,
            )
        }

        data class ImageDoc(
            val title: String,
            val images: List<Image>,
            val summary: String? = null,
            val startTerm: LocalDateTime? = null,
            val endTerm: LocalDateTime? = null
        ) {
            data class Image(
                val order: Int,
                @get:URL
                val path: String,
                @get:URL
                val thumbnail: String? = null
            )
        }
    }

    class Response {

        class View {
            interface Detail
        }

        data class Board(
            val id: Long,
            val topic: String,
            val articles: List<Article>,
            val description: String? = null
        )

        data class Article(
            val id: Long,
            val createdAt: LocalDateTime,
            val modifiedAt: LocalDateTime,
            val title: String,
            val summary: String? = null,
            @JsonView(View.Detail::class)
            val articleParagraph: ArticleParagraph? = null,
            @JsonView(View.Detail::class)
            val attach: List<Attach>? = null,
            val auditor: String? = null
        ) {
            data class ArticleParagraph(
                val id: Long,
                val type: Paragraph.Type,
                @JsonDeserialize(using = KeepAsJsonDeserializer::class)
                @JsonRawValue
                val text: String
            ) {
                companion object {
                    fun from(paragraph: Paragraph): ArticleParagraph {
                        return paragraph.run {
                            ArticleParagraph(
                                id!!, type, text
                            )
                        }
                    }
                }
            }

            data class Attach(
                val id: Long,
                val type: Attachment.Type,
                val order: Int,
                val path: String,
                val thumbnail: String? = null
            ) {
                companion object {
                    fun from(attachment: Attachment): Attach {
                        return attachment.run {
                            Attach(id!!, type, order, path, thumbnail)
                        }
                    }
                }
            }

            companion object {
                fun from(writing: Writing): Article {
                    return writing.run {
                        Article(
                            id!!,
                            createdAt!!,
                            modifiedAt!!,
                            title,
                            summary,
                            paragraph?.let {
                                ArticleParagraph.from(it)
                            },
                            attachments.map {
                                Attach.from(it)
                            }
                        )
                    }
                }
            }
        }
    }
}
