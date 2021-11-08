package io.mustelidae.smallclawedotter.api.domain.board.repository

import io.mustelidae.smallclawedotter.api.domain.board.Document
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DocumentRepository : JpaRepository<Document, Long>
