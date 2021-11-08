package io.mustelidae.smallclawedotter.api.domain.board.api

import io.mustelidae.smallclawedotter.api.domain.board.Paragraph
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
        data class Board(
            val topicName: String
        )
    }
}
