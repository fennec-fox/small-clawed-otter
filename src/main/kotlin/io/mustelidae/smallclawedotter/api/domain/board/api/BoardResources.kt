package io.mustelidae.smallclawedotter.api.domain.board.api

import com.fasterxml.jackson.annotation.JsonIgnore
import io.mustelidae.smallclawedotter.api.domain.board.Attachment
import io.mustelidae.smallclawedotter.api.domain.board.Paragraph
import io.mustelidae.smallclawedotter.api.domain.board.Writing
import org.hibernate.validator.constraints.URL
import java.time.LocalDateTime

class BoardResources {

    class Request {
        data class TextArticle(
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

        data class ImageArticle(
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

    class Modify {
        data class TextArticle(
            val title: String,
            val text: String,
            val summary: String? = null,
            val startTerm: LocalDateTime? = null,
            val endTerm: LocalDateTime? = null,
        ) {
            @JsonIgnore
            fun hasTerm(): Boolean {
                return (startTerm != null && endTerm != null)
            }
        }

        data class ImageArticle(
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

            @JsonIgnore
            fun hasTerm(): Boolean {
                return (startTerm != null && endTerm != null)
            }
        }
    }

    class Reply {

        data class Article(
            val id: Long,
            val createdAt: LocalDateTime,
            val modifiedAt: LocalDateTime,
            val auditor: String,
            val type: Writing.Type,
            val title: String,
            val summary: String? = null,
            val startTerm: LocalDateTime? = null,
            val endTerm: LocalDateTime? = null,
            val hidden: Boolean? = null,
            val showDateTime: LocalDateTime? = null
        ) {
            companion object {
                fun from(writing: Writing): Article {
                    return writing.run {
                        Article(
                            id!!,
                            createdAt!!,
                            modifiedAt!!,
                            auditor!!,
                            type,
                            title,
                            summary,
                            effectiveDate,
                            expirationDate,
                            hidden,
                            showDateTime
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
            val type: Writing.Type,
            val title: String,
            val summary: String? = null,
            val startTerm: LocalDateTime? = null,
            val endTerm: LocalDateTime? = null,
            val hidden: Boolean? = null,
            val showDateTime: LocalDateTime? = null,
            val paragraph: ArticleParagraph? = null,
            val attachments: List<Attach>? = null
        ) {
            private constructor(
                article: Article,
                articleParagraph: ArticleParagraph? = null,
                attach: List<Attach>? = null
            ) : this(
                article.id,
                article.createdAt,
                article.modifiedAt,
                article.auditor,
                article.type,
                article.title,
                article.summary,
                article.startTerm,
                article.endTerm,
                article.hidden,
                article.showDateTime,
                articleParagraph,
                attach
            )

            companion object {
                fun from(writing: Writing): ArticleWithParagraph {
                    val article = Article.from(writing)
                    val paragraph = writing.paragraph?.let { ArticleParagraph.from(it) }
                    val attach = writing.attachments.map { Attach.from(it) }
                    return writing.run {
                        ArticleWithParagraph(
                            article,
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
