package io.mustelidae.smallclawedotter.api.domain.board

import io.mustelidae.smallclawedotter.api.config.DataNotFindException
import io.mustelidae.smallclawedotter.api.domain.board.repository.WritingDSLRepository
import io.mustelidae.smallclawedotter.api.domain.board.repository.WritingRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class WritingFinder(
    private val writingDSLRepository: WritingDSLRepository,
    private val writingRepository: WritingRepository
) {

    fun findOne(id: Long): Writing {
        return writingDSLRepository.findOne(id) ?: throw DataNotFindException(id, "해당 게시글이 존재하지 않습니다.")
    }

    fun findAll(topicCode: String): List<Writing> {
        return writingDSLRepository.findAllByTopic(topicCode) ?: emptyList()
    }
}
