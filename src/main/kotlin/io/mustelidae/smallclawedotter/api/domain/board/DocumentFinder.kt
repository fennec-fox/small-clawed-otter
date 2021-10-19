package io.mustelidae.smallclawedotter.api.domain.board

import io.mustelidae.smallclawedotter.api.config.DataNotFindException
import io.mustelidae.smallclawedotter.api.domain.board.repository.DocumentDSLRepository
import io.mustelidae.smallclawedotter.api.domain.board.repository.DocumentRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class DocumentFinder(
    private val documentDSLRepository: DocumentDSLRepository,
    private val documentRepository: DocumentRepository
) {

    fun findOne(id: Long): Document {
        return documentRepository.findByIdOrNull(id) ?: throw DataNotFindException(id, "해당 게시글이 존재하지 않습니다.")
    }

    fun findAllByTopic(code: String): List<Document> {
        return documentDSLRepository.findAllByTopic(code) ?: emptyList()
    }
}
