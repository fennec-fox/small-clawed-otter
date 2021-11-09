package io.mustelidae.smallclawedotter.api.domain.topic.repository

import io.mustelidae.smallclawedotter.api.common.ProductCode
import io.mustelidae.smallclawedotter.api.domain.topic.Topic
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TopicRepository : JpaRepository<Topic, Long> {

    fun findAllByProductCodeAndExpiredFalse(productCode: ProductCode): List<Topic>?
    fun findByCode(code: String): Topic?
}
