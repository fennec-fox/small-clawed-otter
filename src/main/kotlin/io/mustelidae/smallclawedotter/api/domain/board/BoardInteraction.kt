package io.mustelidae.smallclawedotter.api.domain.board

import io.mustelidae.smallclawedotter.api.domain.board.api.BoardResources
import io.mustelidae.smallclawedotter.api.domain.board.repository.WritingRepository
import io.mustelidae.smallclawedotter.api.domain.topic.Topic
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BoardInteraction(
    private val writingRepository: WritingRepository
) {

    fun write(topic: Topic, request: BoardResources.Request.TextDoc): Writing {
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

        return writingRepository.save(textBaseWriting.writing)
    }

    fun write(topic: Topic, request: BoardResources.Request.ImageDoc): Writing {
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

        return writingRepository.save(imageBaseWriting.writing)
    }
}
