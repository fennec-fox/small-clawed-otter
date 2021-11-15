package io.mustelidae.smallclawedotter.api.domain.board

import io.mustelidae.smallclawedotter.api.config.DataNotFindException
import io.mustelidae.smallclawedotter.api.domain.board.repository.WritingDSLRepository
import io.mustelidae.smallclawedotter.api.domain.board.repository.WritingRepository
import io.mustelidae.smallclawedotter.api.domain.topic.Topic
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class WritingFinder(
    private val writingDSLRepository: WritingDSLRepository,
    private val writingRepository: WritingRepository
) {

    fun findOne(id: Long): Writing {
        return writingRepository.findByIdOrNull(id) ?: throw DataNotFindException(id, "해당 게시글이 존재하지 않습니다.")
    }

    fun findAllByTopicCode(code: String): List<Writing> {
        return writingDSLRepository.findAllByTopic(code) ?: emptyList()
    }

    fun findAll(topic: Topic): List<Writing> {
        return writingDSLRepository.findAllByTopic(topic.code) ?: emptyList()
    }
}
