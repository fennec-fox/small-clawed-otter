package io.mustelidae.smallclawedotter.api.domain.board.repository

import io.mustelidae.smallclawedotter.api.domain.board.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ArticleRepository : JpaRepository<Article, Long>
