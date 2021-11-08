package io.mustelidae.smallclawedotter.api.domain.board

import io.mustelidae.smallclawedotter.api.config.DataNotFindException
import io.mustelidae.smallclawedotter.api.domain.board.repository.ArticleDSLRepository
import io.mustelidae.smallclawedotter.api.domain.board.repository.ArticleRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ArticleFinder(
    private val articleDSLRepository: ArticleDSLRepository,
    private val articleRepository: ArticleRepository
) {

    fun findOne(id: Long): Article {
        return articleRepository.findByIdOrNull(id) ?: throw DataNotFindException(id, "해당 게시글이 존재하지 않습니다.")
    }

    fun findAllByTopic(code: String): List<Article> {
        return articleDSLRepository.findAllByTopic(code) ?: emptyList()
    }
}
