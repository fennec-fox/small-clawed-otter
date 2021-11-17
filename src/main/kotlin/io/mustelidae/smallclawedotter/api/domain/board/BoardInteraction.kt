package io.mustelidae.smallclawedotter.api.domain.board

import io.mustelidae.smallclawedotter.api.domain.board.api.BoardResources
import io.mustelidae.smallclawedotter.api.domain.board.repository.AttachmentRepository
import io.mustelidae.smallclawedotter.api.domain.board.repository.WritingRepository
import io.mustelidae.smallclawedotter.api.domain.topic.TopicFinder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BoardInteraction(
    private val writingRepository: WritingRepository,
    private val writingFinder: WritingFinder,
    private val attachmentRepository: AttachmentRepository,
    private val topicFinder: TopicFinder
) {

    fun write(topicCode: String, request: BoardResources.Request.TextArticle): Writing {
        val topic = topicFinder.findOne(topicCode)
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

    fun modify(writingId: Long, modify: BoardResources.Modify.TextArticle): Writing {
        val writing = writingFinder.findOne(writingId)

        val textBaseWriting = TextBaseWriting(writing)
        textBaseWriting.modify(
            modify.title,
            modify.text,
            modify.summary
        )

        if (modify.hasTerm())
            textBaseWriting.setTerm(modify.startTerm!!, modify.endTerm!!)

        return writingRepository.save(textBaseWriting.writing)
    }

    fun write(topicCode: String, request: BoardResources.Request.ImageArticle): Writing {
        val topic = topicFinder.findOne(topicCode)
        val imageBaseWriting = ImageBaseWriting(topic)

        imageBaseWriting.write(
            request.title,
            request.summary
        )

        request.images.forEach {
            imageBaseWriting.addImage(it.order, it.path, it.thumbnail)
        }

        if (request.startTerm != null && request.endTerm != null)
            imageBaseWriting.setTerm(request.startTerm, request.endTerm)

        return writingRepository.save(imageBaseWriting.writing)
    }

    fun modify(writingId: Long, modify: BoardResources.Modify.ImageArticle): Writing {
        // 기존에 attach된 걸 모두 삭제하고 다시 등록
        attachmentRepository.removeAttachmentsByWritingId(writingId)

        val writing = writingFinder.findOne(writingId)
        val imageBaseWriting = ImageBaseWriting(writing)

        imageBaseWriting.modify(
            modify.title,
            modify.summary
        )

        modify.images.forEach {
            imageBaseWriting.addImage(it.order, it.path, it.thumbnail)
        }

        if (modify.hasTerm())
            imageBaseWriting.setTerm(modify.startTerm!!, modify.endTerm!!)

        return writingRepository.save(imageBaseWriting.writing)
    }
}
