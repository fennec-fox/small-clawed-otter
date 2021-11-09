package io.mustelidae.smallclawedotter.api.domain.topic

import io.mustelidae.smallclawedotter.api.common.ProductCode
import io.mustelidae.smallclawedotter.api.config.DataNotFindException
import io.mustelidae.smallclawedotter.api.domain.topic.repository.TopicRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class TopicFinder(
    private val topicRepository: TopicRepository
) {

    fun findOne(code: String): Topic {
        val topic = topicRepository.findByCode(code) ?: throw DataNotFindException("해당 토픽이 존재하지 않습니다.")
        if (topic.expired)
            throw DataNotFindException("해당 토픽이 존재하지 않습니다.")
        return topic
    }

    fun findAll(productCode: ProductCode): List<Topic> {
        return topicRepository.findAllByProductCodeAndExpiredFalse(productCode) ?: emptyList()
    }
}
