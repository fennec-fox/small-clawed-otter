package io.mustelidae.smallclawedotter.api.domain.board.api

import io.mustelidae.smallclawedotter.api.domain.board.Attachment
import io.mustelidae.smallclawedotter.api.domain.board.Paragraph
import io.mustelidae.smallclawedotter.api.domain.board.Writing
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

    class Reply {

        data class ArticleSummary(
            val id: Long,
            val createdAt: LocalDateTime,
            val modifiedAt: LocalDateTime,
            val auditor: String,
            val title: String,
            val summary: String? = null
        ) {
            companion object {
                fun from(writing: Writing): ArticleSummary {
                    return writing.run {
                        ArticleSummary(
                            id!!,
                            createdAt!!,
                            modifiedAt!!,
                            auditor!!,
                            title,
                            summary
                        )
                    }
                }
            }
        }

        data class ArticleWithParagraph(
            val id: Long,
            val createdAt: LocalDateTime,
            val modifiedAt: LocalDateTime,
            val auditor: String,
            val title: String,
            val summary: String? = null,
            val articleParagraph: ArticleParagraph? = null,
            val attach: List<Attach>? = null
        ) {
            private constructor(
                articleSummary: ArticleSummary,
                articleParagraph: ArticleParagraph? = null,
                attach: List<Attach>? = null
            ) : this(
                articleSummary.id,
                articleSummary.createdAt,
                articleSummary.modifiedAt,
                articleSummary.auditor,
                articleSummary.title,
                articleSummary.summary,
                articleParagraph,
                attach
            )

            companion object {
                fun from(writing: Writing): ArticleWithParagraph {
                    val articleSummary = ArticleSummary.from(writing)
                    val paragraph = writing.paragraph?.let { ArticleParagraph.from(it) }
                    val attach = writing.attachments.map { Attach.from(it) }
                    return writing.run {
                        ArticleWithParagraph(
                            articleSummary,
                            paragraph,
                            attach
                        )
                    }
                }
            }

            data class ArticleParagraph(
                val id: Long,
                val type: Paragraph.Type,
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
        }
    }
}
