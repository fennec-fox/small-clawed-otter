package io.mustelidae.smallclawedotter.api.domain.board

import io.mustelidae.smallclawedotter.api.domain.board.api.BoardResources
import io.mustelidae.smallclawedotter.api.domain.board.repository.ArticleRepository
import io.mustelidae.smallclawedotter.api.domain.topic.Topic
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class NoticeBoardInteraction(
    private val articleRepository: ArticleRepository
) {

    fun write(topic: Topic, request: BoardResources.Request.TextDoc): Article {
        val textBaseWriting = TextBaseWriting(topic)

        textBaseWriting.write(
            request.title,
            request.type,
            request.text,
            request.summary
        )

        if (request.startTerm != null && request.endTerm != null)
            textBaseWriting.setTerm(request.startTerm, request.endTerm)

        request.files?.let {
            for (file in it) {
                textBaseWriting.addAttachment(
                    Attachment.Type.FILE,
                    file.order,
                    file.path
                )
            }
        }

        return articleRepository.save(textBaseWriting.article)
    }

    fun write(topic: Topic, request: BoardResources.Request.ImageDoc): Article {
        val imageBaseWriting = ImageBaseWriting(topic)
        val attachments = request.images.map {
            Attachment(Attachment.Type.IMAGE, it.order, it.path).apply { this.thumbnail = it.thumbnail }
        }
        imageBaseWriting.write(
            request.title,
            attachments,
            request.summary
        )

        if (request.startTerm != null && request.endTerm != null)
            imageBaseWriting.setTerm(request.startTerm, request.endTerm)

        return articleRepository.save(imageBaseWriting.article)
    }
}
